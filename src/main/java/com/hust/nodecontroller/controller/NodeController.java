package com.hust.nodecontroller.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.*;
import com.hust.nodecontroller.service.ControlProcess;
import com.hust.nodecontroller.service.ControlProcessImpl;
import com.hust.nodecontroller.service.NodeService;
import com.hust.nodecontroller.utils.CalStateUtil;
import com.hust.nodecontroller.utils.GetSysInfoUtil;
import com.hust.nodecontroller.utils.IndustryQueryUtil;
import org.omg.CORBA.BAD_CONTEXT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @Bean("threadNum")
    AtomicInteger Num()
    {
        return threadNum;
    }


    private final NodeService nodeService;
    public String ipAndPort;
    public String serverIp;
    public static AtomicInteger threadNum = new AtomicInteger(0);
    private static final Logger logger = LoggerFactory.getLogger(NodeController.class);

    @Autowired
    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    /**
     * @Description : 注册标识
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
            backHtml.setMessage("注册标识信息成功！");
            threadNum.decrementAndGet();
            return backHtml;

        } catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            threadNum.decrementAndGet();
            return backHtml;
        }
    }

    /**
     * 删除标识
     * @param infoFromClient
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public NormalMsg delete(@RequestBody(required = false) InfoFromClient infoFromClient) throws Exception {
        NormalMsg backHtml = new NormalMsg();
        try {
            threadNum.addAndGet(1);
            nodeService.delete(infoFromClient);
            backHtml.setStatus(1);
            backHtml.setMessage("删除标识信息成功！");
            threadNum.decrementAndGet();
            return backHtml;

        } catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            threadNum.decrementAndGet();
            return backHtml;
        }
    }

    /**
     * 更新标识
     * @param infoFromClient
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public NormalMsg update(@RequestBody InfoFromClient infoFromClient) throws Exception {
        NormalMsg backHtml = new NormalMsg();
        try {
            threadNum.addAndGet(1);
            nodeService.update(infoFromClient);
            backHtml.setStatus(1);
            backHtml.setMessage("更新标识信息成功！");
            threadNum.decrementAndGet();
            return backHtml;

        } catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            threadNum.decrementAndGet();
            return backHtml;
        }
    }

    /**
     * 查询标识
     * @param infoFromClient
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public QueryResult query(@RequestBody InfoFromClient infoFromClient) throws Exception {
        QueryResult backHtml = new QueryResult();
        try {
            threadNum.addAndGet(1);
            backHtml = nodeService.query(infoFromClient);
            backHtml.setStatus(1);
            backHtml.setMessage("查询标识信息成功！");
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


    /**
     * 根据企业前缀返回所有标识信息
     * @param infoFromClient
     * @return
     * @throws Exception
     */
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

    /**
     * 获取当前企业服务器的CPU占用率、内存占用率
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/system-info")
    @ResponseBody
    public SystemState getSystemInfo() throws Exception {
        SystemState backHtml = new SystemState();
        try {
            backHtml.setData(GetSysInfoUtil.getMinuteSysInfo());
            backHtml.setStatus(1);
            backHtml.setMessage("Success!");
            return backHtml;
        }catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            return backHtml;
        }
    }

    /**
     * 当前服务器的解析量、注册量
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/runtime-info1")
    @ResponseBody
    public RuntimeState getRuntimeInfo1() throws Exception {
        RuntimeState backHtml= new RuntimeState();
        try {
            backHtml.setData(CalStateUtil.getRuntimeInfoList1());
            backHtml.setStatus(1);
            backHtml.setMessage("Success!");
            return backHtml;

        }catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            return backHtml;
        }
    }

    /**
     * 当前服务器的解析正确率、总流量以及解析时延
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/runtime-info2")
    @ResponseBody
    public RuntimeState getRuntimeInfo2() throws Exception {
        RuntimeState backHtml= new RuntimeState();
        try {
            backHtml.setData(CalStateUtil.getRuntimeInfoList2());
            backHtml.setStatus(1);
            backHtml.setMessage("Success!");
            return backHtml;

        }catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            return backHtml;
        }
    }

    /**
     * 获取当前企业服务器的资源信息（标识总数、解析总数、内存总数、磁盘总量）
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/resource-info")
    @ResponseBody
    public ResourceInfo getResourceInfo() throws Exception {
        ResourceInfo backHtml = new ResourceInfo();
        try {
            backHtml.setMemTotal(GetSysInfoUtil.MemTotal());
            backHtml.setDiskTotal(GetSysInfoUtil.DiskTotal());
            backHtml.setQueryCount(CalStateUtil.queryCount);
            backHtml.setIdCount(nodeService.queryNodeIdTotal());

            if (CalStateUtil.queryCount == 0)
                backHtml.setQueryTimeout(0);
            else
                backHtml.setQueryTimeout((float) CalStateUtil.queryTimeout / CalStateUtil.queryCount);

            backHtml.setStatus(1);
            backHtml.setMessage("企业服务器资源信息查询成功");
            return backHtml;
        }catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            return backHtml;
        }

    }

    /**
     * 根据企业前缀获取该前缀下所有标识的解析排名
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/identityRank")
    @ResponseBody
    public IdentityRankInfo queryIdentityRank(@RequestBody JSONObject param) throws Exception {
        try {
            String prefix = param.getString("prefix");
            return nodeService.queryIdRankByPrefix(prefix);
        }catch (Exception e) {
            IdentityRankInfo backHtml = new IdentityRankInfo();
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            return backHtml;
        }
    }

    @RequestMapping(value = "/hidInfo")
    @ResponseBody
    public HidInfo queryHidInfo() {
        HidInfo backHtml = new HidInfo();
        backHtml.setOidCount(CalStateUtil.differOid());
        backHtml.setEcodeCount(CalStateUtil.differEcode());
        backHtml.setHandleCount(CalStateUtil.differHandle());
        backHtml.setDnsCount(CalStateUtil.differDns());
        CalStateUtil.preOidQueryCount = CalStateUtil.oidQueryCount;
        CalStateUtil.preEcodeQueryCount = CalStateUtil.ecodeQueryCount;
        CalStateUtil.preHandleQueryCount = CalStateUtil.handleQueryCount;
        CalStateUtil.preDnsQueryCount = CalStateUtil.dnsQueryCount;
        backHtml.setStatus(1);
        backHtml.setMessage("异构查询成功！");
        return backHtml;
    }

    @RequestMapping(value = "/industryInfo")
    @ResponseBody
    public IndustryInfo queryIndustryInfo() throws InterruptedException {
        IndustryInfo backHtml = new IndustryInfo();
        backHtml.setIndustryName("086.001");
        backHtml.setDataCount(IndustryQueryUtil.calIndustryQueryInfo());
        backHtml.setStatus(1);
        backHtml.setMessage("行业时段解析查询成功！");
        return backHtml;
    }

    @RequestMapping(value = "bulkRegister")
    @ResponseBody
    public NormalMsg bulkRegister(@RequestBody BulkRegister bulkRegister) throws Exception {
        NormalMsg backHtml = new NormalMsg();
        int idCount = bulkRegister.getData().size();
        CalStateUtil.registerCount = CalStateUtil.registerCount + idCount;
        CalStateUtil.totalCount = CalStateUtil.totalCount + idCount;
        try {
            threadNum.addAndGet(idCount);
            idCount = nodeService.bulkRegister(bulkRegister);
            backHtml.setStatus(1);
            backHtml.setMessage("批量注册标识信息成功!已成功注册" + idCount + "个标识");
            threadNum.decrementAndGet();
            return backHtml;

        } catch (Exception e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            threadNum.decrementAndGet();
            return backHtml;
        }
    }




    // ################################### 以下方法弃用 ################################################
    /**
     * 批量查询
     * @param jsonArray
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "bulk-query")
    @ResponseBody
    @Deprecated
    public BulkInfo bulkQuery(@RequestBody JSONArray jsonArray) throws Exception {
        return nodeService.bulkQuery(jsonArray);
    }

    @RequestMapping(value = "/nodeState")
    @ResponseBody
    @Deprecated
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

    /**
     * 查询整个系统的节点总数、行业总数、平均解析时延
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/systemState")
    @ResponseBody
    @Deprecated
    public SystemTotalState querySystemState() throws Exception {
        SystemTotalState backHtml = new SystemTotalState();
        try{
            backHtml = nodeService.querySystemTotalState();
            return backHtml;
        }catch (Exception e){
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            return backHtml;
        }
    }
}
