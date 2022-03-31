package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONArray;
import com.hust.nodecontroller.enums.IdentityTypeEnum;
import com.hust.nodecontroller.infostruct.*;
import com.hust.nodecontroller.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName NodeServiceImpl
 * @date 2020.09.20 17:05
 */

@Service
public class NodeServiceImpl implements NodeService{

    private final String serverIp;
    private final String dhtPort;
    private final String controllerPort;
    private final String zookeeperAddress;

    private final String dhtRegisterUrl;
    private final String dhtDeleteUrl;
    private final String dhtUpdateUrl;
    private final String dhtQueryUrl;
    private final String dhtAllNode;
    private final String dhtFindAllId;
    private final String dhtBulkRegisterUrl;
    private final String dhtBulkQueryUrl;
    private final String dhtOrgResolveNumsUrl;

    @Value("${bc.register.url}")
    private String bcRegisterUrl;
    @Value("${bc.delete.url}")
    private String bcDeleteUrl;
    @Value("${bc.update.url}")
    private String bcUpdateUrl;
    @Value("${bc.query.url}")
    private String bcQueryUrl;
    @Value("${bc.queryPrefix.url}")
    private String bcPrefixQuery;
    @Value("${bc.queryOwnerByPrefix.url}")
    private String bcQueryOwner;

    private final ControlProcess controlProcess;
    private static final Logger logger = LoggerFactory.getLogger(NodeServiceImpl.class);

    @Autowired
    public NodeServiceImpl(@Qualifier("controlProcessImpl")ControlProcess controlProcess, ApplicationArguments applicationArguments) {
        this.controlProcess = controlProcess;

        List<String> ip = applicationArguments.getOptionValues("ip");
        List<String> dPort = applicationArguments.getOptionValues("dPort");
        List<String> cPort = applicationArguments.getOptionValues("server.port");
        List<String> zAddress = applicationArguments.getOptionValues("zAddress");
        serverIp = ip.get(0);
        dhtPort = dPort.get(0);
        controllerPort = cPort.get(0);
        zookeeperAddress = zAddress.get(0);

        dhtRegisterUrl = "http://" + serverIp + ":" + dhtPort + "/dht/register";
        dhtDeleteUrl = "http://" + serverIp + ":" + dhtPort + "/dht/delete";
        dhtUpdateUrl = "http://" + serverIp + ":" + dhtPort + "/dht/modify";
        dhtQueryUrl = "http://" + serverIp + ":" + dhtPort + "/dht/resolve";
        dhtAllNode = "http://" + serverIp + ":" + dhtPort + "/dht/printList";
        dhtFindAllId = "http://" + serverIp + ":" + dhtPort + "/dht/findAllId";
        dhtBulkRegisterUrl = "http://" + serverIp + ":" + dhtPort + "/dht/registers";
        dhtBulkQueryUrl = "http://" + serverIp + ":" + dhtPort + "/dht/resolves";
        dhtOrgResolveNumsUrl = "http://" + serverIp + ":" + dhtPort + "/dht/getOrgResolveNums";
    }

    @Bean
    List<String> serverInfo() {
        List<String > infoList = new LinkedList<>();
        infoList.add(serverIp);
        infoList.add(dhtPort);
        infoList.add(controllerPort);
        infoList.add(zookeeperAddress);
        return infoList;
    }

    @Override
    public void register(InfoFromClient infoFromClient) throws Exception {
        controlProcess.enterpriseHandle(infoFromClient,dhtRegisterUrl,bcRegisterUrl,8, false);
    }

    @Override
    public int bulkRegister(BulkRegister bulkRegister) throws Exception {
        return controlProcess.bulkRegister(bulkRegister,dhtRegisterUrl,bcRegisterUrl);
    }

    @Override
    public void delete(InfoFromClient infoFromClient) throws Exception {
        controlProcess.enterpriseHandle(infoFromClient,dhtDeleteUrl,bcDeleteUrl,4, false);
    }

    @Override
    public void update(InfoFromClient infoFromClient) throws Exception {
        controlProcess.enterpriseHandle(infoFromClient,dhtUpdateUrl,bcUpdateUrl,2, false);
    }

    @Override
    public QueryResult query(InfoFromClient infoFromClient) throws Exception {
        return multipleTypeQuery(infoFromClient,false);
    }

    @Override
    public QueryResult multipleTypeQuery(InfoFromClient infoFromClient, boolean isDnsQuery) throws Exception {
        String identification = infoFromClient.getIdentification();
        String client = infoFromClient.getClient();
        String encType = infoFromClient.getType();

        // 添加解密，解密出明文
        if ("sm2".equals(encType)) {
            identification = EncDecUtil.sMDecrypt(identification);
            client = EncDecUtil.sMDecrypt(client);
        }
        else if ("rsa".equals(encType)) {
            identification = EncDecUtil.rsaDecrypt(identification);
            client = EncDecUtil.rsaDecrypt(client);
        }

        QueryResult queryResult = new QueryResult();

        switch (IdTypeJudgeUtil.typeJudge(identification)) {
            case IDENTITY_TYPE_OID:
                queryResult.setGoodsInfo(IdTypeJudgeUtil.oidResolve(identification, encType));
                break;
            case IDENTITY_TYPE_HANDLE:
                queryResult.setGoodsInfo(IdTypeJudgeUtil.handleResolve(identification, encType));
                break;
            case IDENTITY_TYPE_ECODE:
                queryResult.setGoodsInfo(IdTypeJudgeUtil.ecodeResolve(identification, encType));
                break;
            case IDENTITY_TYPE_DHT:
                queryResult = controlProcess.userHandle(identification, client, dhtQueryUrl,bcQueryUrl, bcQueryOwner, encType);
                break;
            case IDENTITY_TYPE_DNS:
                queryResult.setGoodsInfo(IdTypeJudgeUtil.dnsResolve(identification, encType));
                break;
            default:
                throw new Exception(IdentityTypeEnum.IDENTITY_TYPE_NOT_SUPPORT.getIdTypeMessage());
        }
        return queryResult;
    }

    @Override
    public BulkInfo bulkQuery(JSONArray jsonArray) {
        return controlProcess.bulkQuery(jsonArray,dhtBulkQueryUrl);
    }

    @Override
    public IdentityInfo queryAllByPrefix(InfoFromClient infoFromClient) throws Exception {
        return controlProcess.identityHandle(infoFromClient,bcPrefixQuery);
    }

    @Override
    public NodeState queryNodeState() throws Exception {
        JSONObject callJson = new JSONObject();
        callJson.put("type", 9);
        return PostRequestUtil.getAllNodeState(dhtAllNode,callJson);
    }

    @Override
    public SystemTotalState querySystemTotalState() throws Exception {
        JSONObject callJson = new JSONObject();
        callJson.put("type", 9);
        return PostRequestUtil.getSystemTotalState(dhtAllNode,callJson);
    }

    @Override
    public int queryNodeIdTotal() throws Exception {
        JSONObject callJson = new JSONObject();
        callJson.put("orgname","controller");
        callJson.put("type",0);
        return PostRequestUtil.QueryNodeIdTotal(dhtFindAllId,callJson);
    }


    @Override
    public IdentityRankInfo queryIdRankByPrefix(String prefix) throws Exception {
        JSONObject callJson = new JSONObject();
        callJson.put("orgname", prefix);
        callJson.put("type", 5);
        return PostRequestUtil.queryIdRankByPrefix(dhtOrgResolveNumsUrl,callJson);
    }

}
