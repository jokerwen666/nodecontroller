package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONArray;
import com.hust.nodecontroller.controller.NodeController;
import com.hust.nodecontroller.exception.AuthorityTestException;
import com.hust.nodecontroller.exception.FormatException;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    private final String dhtBulkRegisterUrl;
    private final String dhtBulkQueryUrl;

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
        dhtBulkRegisterUrl = "http://" + serverIp + ":" + dhtPort + "/dht/registers";
        dhtBulkQueryUrl = "http://" + serverIp + ":" + dhtPort + "/dht/resolves";
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
        controlProcess.enterpriseHandle(infoFromClient,dhtRegisterUrl,bcRegisterUrl,8);
    }

    @Override
    public BulkInfo bulkRegister(JSONArray jsonArray) {
        return controlProcess.bulkRegister(jsonArray,dhtBulkRegisterUrl);
    }

    @Override
    public void delete(InfoFromClient infoFromClient) throws Exception {
        controlProcess.enterpriseHandle(infoFromClient,dhtDeleteUrl,bcDeleteUrl,4);
    }

    @Override
    public void update(InfoFromClient infoFromClient) throws Exception {
        controlProcess.enterpriseHandle(infoFromClient,dhtUpdateUrl,bcUpdateUrl,2);
    }

    @Override
    public QueryResult query(InfoFromClient infoFromClient) throws Exception {
        String identification = infoFromClient.getIdentification();
        QueryResult queryResult = new QueryResult();
        switch (IdTypeJudgeUtil.TypeJudge(infoFromClient.getIdentification())) {
            case 1 : //oid
                queryResult.setGoodsInfo(IdTypeJudgeUtil.oidResolve(identification));
                break;
            case 2 : //handle
                queryResult.setGoodsInfo(IdTypeJudgeUtil.handleResolve(identification));
                break;
            case 3 : //ecode
                queryResult.setGoodsInfo(IdTypeJudgeUtil.ecodeResolve(identification));
                break;
            case 4 : //创新型
                queryResult = controlProcess.userHandle(infoFromClient,dhtQueryUrl,bcQueryUrl);
                break;
            default:
                //提示标识不合法
                break;
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

}
