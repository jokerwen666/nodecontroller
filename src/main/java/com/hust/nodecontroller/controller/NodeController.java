package com.hust.nodecontroller.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.*;
import com.hust.nodecontroller.service.NodeService;
import com.hust.nodecontroller.utils.CalStateUtil;
import com.hust.nodecontroller.utils.GetSysInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * QUESTION：
 * 如果发送的操作类型被隐藏在签名的密文中，是否可以还需要对端口进行分划？
 * 如果对端口进行分划，是否还需要前端向后端传递操作类型的参数？

 * @author Zhang Bowen
 * @Description
 * 该控制器用于模拟“控制子系统
 * 当接受到接入客户端发送信息时，按照请求类型（增删改查）进行相应操作
 *
 * @ClassName NodeController
 * @date 2020.09.14 16:13
 */

@Controller
@CrossOrigin
@RequestMapping(value = "/api")
public class NodeController {

    public String ipAndPort;

    @Bean("threadNum")
    AtomicInteger Num()
    {
        return threadNum;
    }

    @Bean("IPAndPort")
    String IPAndPort() { return ipAndPort; }


    private final NodeService nodeService;
    public static AtomicInteger threadNum = new AtomicInteger(0);
    private static final Logger logger = LoggerFactory.getLogger(NodeController.class);

    @Autowired
    public NodeController(NodeService nodeService, ApplicationArguments applicationArguments) {
        this.nodeService = nodeService;
        List<String> ip = applicationArguments.getOptionValues("Ip");
        ipAndPort = ip.get(0) + ":" + "10400";
        System.out.println("Server's IP and port is: " + ipAndPort);
        //logger.info(this.ipAndPort);
    }

    /**
     * @Description : 向dht网络中注册标识信息
     * @author : Zhang Bowen
     * @date :  10:42
     * @param infoFromClient 从客户端中接收到的信息，形如<标识，hash(标识、操作类型、url、产品信息摘要、对标识操作权限)>:
     * @return : com.hust.nodecontroller.infostruct.NormalMsg
     */

    @SentinelResource("DHT Register")
    @RequestMapping(value = "/register")
    @ResponseBody
    public NormalMsg register(@RequestBody(required = false) InfoFromClient infoFromClient) throws Exception {
        NormalMsg backHtml = new NormalMsg();
        try {
            threadNum.addAndGet(1);
            nodeService.register(infoFromClient);
            backHtml.setStatus(1);
            backHtml.setMessage("Success!");
            threadNum.decrementAndGet();
            return backHtml;

        } catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            threadNum.decrementAndGet();
            return backHtml;
        }
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public NormalMsg delete(@RequestBody(required = false) InfoFromClient infoFromClient) throws Exception {
        NormalMsg backHtml = new NormalMsg();
        try {
            threadNum.addAndGet(1);
            nodeService.delete(infoFromClient);
            backHtml.setStatus(1);
            backHtml.setMessage("Success!");
            threadNum.decrementAndGet();
            return backHtml;

        } catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            threadNum.decrementAndGet();
            return backHtml;
        }
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public NormalMsg update(@RequestBody InfoFromClient infoFromClient) throws Exception {
        NormalMsg backHtml = new NormalMsg();
        try {
            threadNum.addAndGet(1);
            nodeService.update(infoFromClient);
            backHtml.setStatus(1);
            backHtml.setMessage("Success!");
            threadNum.decrementAndGet();
            return backHtml;

        } catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            threadNum.decrementAndGet();
            return backHtml;
        }
    }

    @RequestMapping(value = "/query")
    @ResponseBody
    public QueryResult query(@RequestBody InfoFromClient infoFromClient) throws Exception {
        QueryResult backHtml = new QueryResult();
        try {
            threadNum.addAndGet(1);
            backHtml = nodeService.query(infoFromClient);
            backHtml.setStatus(1);
            backHtml.setMessage("Success!");
            CalStateUtil.successCount++;
            threadNum.decrementAndGet();
            return backHtml;
        } catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            threadNum.decrementAndGet();
            return backHtml;
        }
    }

    @RequestMapping(value = "/nodeState")
    @ResponseBody
    public NodeState queryNodeState() throws Exception {
        NodeState backHtml = new NodeState();
        try{
            backHtml = nodeService.queryNodeState();
            return backHtml;
        }catch (Exception e){
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            return backHtml;
        }
    }

    @RequestMapping(value = "/queryAllByPrefix")
    @ResponseBody
    public IdentityInfo queryAllByPrefix(@RequestBody InfoFromClient infoFromClient) throws Exception {
        IdentityInfo backHtml = new IdentityInfo();
        try{
            backHtml = nodeService.queryAllByPrefix(infoFromClient);
            return backHtml;
        }catch (Exception e){
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            return backHtml;
        }
    }



    @RequestMapping(value = "/runtime-info")
    @ResponseBody
    public RuntimeState getRuntimeInfo() throws Exception {
        RuntimeState backHtml= new RuntimeState();
        try {
            backHtml.setQueryCount(CalStateUtil.differQuery());
            backHtml.setRegisterCount(CalStateUtil.differRegister());
            backHtml.setSuccessRate(CalStateUtil.getSuccessRate());
            backHtml.setTotalCount(CalStateUtil.differTotal());
            backHtml.setStatus(1);
            backHtml.setMessage("Success!");
            CalStateUtil.updateState();
            return backHtml;

        }catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            CalStateUtil.updateState();
            return backHtml;
        }
    }

    @RequestMapping(value = "/system-info")
    @ResponseBody
    public SystemState getSystemInfo() throws Exception {
        SystemState backHtml = new SystemState();
        try {
            backHtml.setCpuRate(GetSysInfoUtil.CpuInfo());
            backHtml.setMemRate(GetSysInfoUtil.MemInfo());
            backHtml.setStatus(1);
            backHtml.setMessage("Success!");
            return backHtml;
        }catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            return backHtml;
        }
    }
}
