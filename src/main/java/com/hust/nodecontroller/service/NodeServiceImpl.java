package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONArray;
import com.hust.nodecontroller.enums.EncryptTypeEnum;
import com.hust.nodecontroller.enums.IdentityTypeEnum;
import com.hust.nodecontroller.enums.RequestTypeEnum;
import com.hust.nodecontroller.exception.ControlSubSystemException;
import com.hust.nodecontroller.infostruct.*;
import com.hust.nodecontroller.infostruct.AnswerStruct.AllPrefixIdAnswer;
import com.hust.nodecontroller.infostruct.AnswerStruct.IdentityInfo;
import com.hust.nodecontroller.infostruct.RequestStruct.*;
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
import java.util.concurrent.ExecutionException;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName NodeServiceImpl
 * @date 2020.09.20 17:05
 */

@Service
public class NodeServiceImpl implements NodeService{
    // 服务器地址与接口信息
    private final String serverIp;
    private final String dhtPort;
    private final String controllerPort;
    private final String zookeeperAddress;

    // 标识管理子系统url
    private final String dhtRegisterUrl;
    private final String dhtDeleteUrl;
    private final String dhtUpdateUrl;
    private final String dhtQueryUrl;
    private final String dhtAllNode;
    private final String dhtFindAllId;
    private final String dhtBulkRegisterUrl;
    private final String dhtBulkQueryUrl;
    private final String dhtOrgResolveNumsUrl;

    // 解析结果验证子系统url
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
    public void register(RegisterIdRequest registerIdRequest) throws ControlSubSystemException {
        String client = registerIdRequest.getClient();
        String identity = registerIdRequest.getIdentification();
        JSONObject data = registerIdRequest.getData();
        String prefix = registerIdRequest.getPrefix();
        controlProcess.registerAndUpdateHandle(client,identity,prefix,data,dhtRegisterUrl,bcRegisterUrl,RequestTypeEnum.REQUEST_TYPE_REGISTER);
    }

    @Override
    public void delete(DeleteIdRequest deleteIdRequest) throws ControlSubSystemException {
        String client = deleteIdRequest.getClient();
        String identity = deleteIdRequest.getIdentification();
        String prefix = deleteIdRequest.getPrefix();
        controlProcess.deleteHandle(client,identity,prefix,dhtDeleteUrl,bcDeleteUrl,RequestTypeEnum.REQUEST_TYPE_DELETE);
    }

    @Override
    public void update(UpdateIdRequest updateIdRequest) throws ControlSubSystemException {
        String client = updateIdRequest.getClient();
        String identity = updateIdRequest.getIdentification();
        JSONObject data = updateIdRequest.getData();
        String prefix = updateIdRequest.getPrefix();
        controlProcess.registerAndUpdateHandle(client,identity,prefix,data,dhtUpdateUrl,bcUpdateUrl,RequestTypeEnum.REQUEST_TYPE_UPDATE);
    }

    @Override
    public QueryResult multipleTypeQuery(QueryIdRequest queryIdRequest, boolean isDnsQuery) throws ControlSubSystemException {
        String identification = queryIdRequest.getIdentification();
        String client = queryIdRequest.getClient();
        String hashType = queryIdRequest.getType();

        // 添加解密，解密出明文
        if (hashType.equals(EncryptTypeEnum.ENCRYPT_TYPE_SM2.getTypeString())) {
            identification = EncDecUtil.sMDecrypt(identification);
            client = EncDecUtil.sMDecrypt(client);
        }
        else if (hashType.equals(EncryptTypeEnum.ENCRYPT_TYPE_RSA.getTypeString())) {
            identification = EncDecUtil.rsaDecrypt(identification);
            client = EncDecUtil.rsaDecrypt(client);
        }

        QueryResult queryResult = new QueryResult();

        if (isDnsQuery) {
            queryResult.setGoodsInfo(IdTypeJudgeUtil.dnsResolve(identification));
            return queryResult;
        }

        switch (IdTypeJudgeUtil.typeJudge(identification)) {
            case IDENTITY_TYPE_OID:
                queryResult.setGoodsInfo(IdTypeJudgeUtil.oidResolve(identification));
                break;
            case IDENTITY_TYPE_HANDLE:
                queryResult.setGoodsInfo(IdTypeJudgeUtil.handleResolve(identification));
                break;
            case IDENTITY_TYPE_ECODE:
                queryResult.setGoodsInfo(IdTypeJudgeUtil.ecodeResolve(identification));
                break;
            case IDENTITY_TYPE_DHT:
                String prefix = queryIdRequest.getPrefix();
                String domainPrefix = queryIdRequest.getDomainPrefix();
                queryResult = controlProcess.queryHandle(client, identification, prefix, domainPrefix, dhtQueryUrl,bcQueryUrl, bcQueryOwner);
                break;
            default:
                throw new ControlSubSystemException(IdentityTypeEnum.IDENTITY_TYPE_NOT_SUPPORT.getIdTypeMessage());
        }
        return queryResult;
    }

    @Override
    public int bulkRegister(BulkRegister bulkRegister) throws Exception {
        return controlProcess.bulkRegister(bulkRegister,dhtRegisterUrl,bcRegisterUrl);
    }


    @Override
    public AllPrefixIdAnswer queryAllByPrefix(QueryAllPrefixIdRequest queryAllPrefixIdRequest) throws ControlSubSystemException {
        String prefix = queryAllPrefixIdRequest.getOrgPrefix();
        String client = queryAllPrefixIdRequest.getClient();
        String matchString = queryAllPrefixIdRequest.getMatchString();
        return controlProcess.queryAllIdByPrefix(prefix,client,matchString,bcPrefixQuery);
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
