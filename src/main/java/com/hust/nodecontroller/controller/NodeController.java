package com.hust.nodecontroller.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.*;
import com.hust.nodecontroller.service.NodeService;
import com.hust.nodecontroller.utils.CalStateUtil;
import com.hust.nodecontroller.utils.GetSysInfoUtil;
import com.hust.nodecontroller.utils.IndustryQueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program nodecontroller
 * @Description 控制子系统控制器
 * @Author jokerwen666
 * @Date  2022-01-18 19:28
 **/
@Controller
@CrossOrigin
@RequestMapping(value = "/api")
public class NodeController {

    @Bean("threadNum")
    AtomicInteger getThreadNum()
    {
        return threadNum;
    }


    private final NodeService nodeService;
    public static AtomicInteger threadNum = new AtomicInteger(0);
    private static final Logger logger = LoggerFactory.getLogger(NodeController.class);

    @Autowired
    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    /**
    * @Description 标识注册接口
    * @Param  infoFromClient
    * @return com.hust.nodecontroller.infostruct.NormalMsg
    * @Author jokerwen666
    * @Date   2022/1/18
    */
    @SentinelResource("DHT Register")
    @RequestMapping(value = "/register")
    @ResponseBody
    public NormalMsg register(@RequestBody(required = false) InfoFromClient infoFromClient) {
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
    * @Description 标识删除接口
    * @Param  infoFromClient
    * @return com.hust.nodecontroller.infostruct.NormalMsg
    * @Author jokerwen666
    * @Date   2022/1/18
    */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public NormalMsg delete(@RequestBody(required = false) InfoFromClient infoFromClient) {
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
    * @Description 标识更新接口
    * @Param  infoFromClient
    * @return com.hust.nodecontroller.infostruct.NormalMsg
    * @Author jokerwen666
    * @Date   2022/1/18
    */
    @RequestMapping(value = "/update")
    @ResponseBody
    public NormalMsg update(@RequestBody InfoFromClient infoFromClient) {
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
    * @Description 标识解析接口
    * @Param  infoFromClient
    * @return com.hust.nodecontroller.infostruct.QueryResult
    * @Author jokerwen666
    * @Date   2022/1/18
    */
    @RequestMapping(value = "/query")
    @ResponseBody
    public QueryResult query(@RequestBody InfoFromClient infoFromClient) {
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
    * @Description 根据企业前缀返回该企业下所有的标识信息
    * @Param  infoFromClient
    * @return com.hust.nodecontroller.infostruct.IdentityInfo
    * @Author jokerwen666
    * @Date   2022/1/18
    */
    @RequestMapping(value = "/queryAllByPrefix")
    @ResponseBody
    public IdentityInfo queryAllByPrefix(@RequestBody InfoFromClient infoFromClient) {
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
    * @Description 获取该企业节点下的cpu和内存使用率
    * @Param
    * @return com.hust.nodecontroller.infostruct.SystemState
    * @Author jokerwen666
    * @Date   2022/1/18
    */
    @RequestMapping(value = "/system-info")
    @ResponseBody
    public SystemState getSystemInfo() {
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
    * @Description 返回当前企业节点的注册量和解析量
    * @Param
    * @return com.hust.nodecontroller.infostruct.RuntimeState
    * @Author jokerwen666
    * @Date   2022/1/18
    */
    @RequestMapping(value = "/runtime-info1")
    @ResponseBody
    public RuntimeState getRuntimeInfo1() {
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
    * @Description 获取当前企业节点的解析成功率、平均解析时延和QPS
    * @Param
    * @return com.hust.nodecontroller.infostruct.RuntimeState
    * @Author jokerwen666
    * @Date   2022/1/18
    */
    @RequestMapping(value = "/runtime-info2")
    @ResponseBody
    public RuntimeState getRuntimeInfo2() {
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
    * @Description 获取当前企业节点的资源信息（标识总数、解析总数、内存总数、磁盘总量）
    * @Param
    * @return com.hust.nodecontroller.infostruct.ResourceInfo
    * @Author jokerwen666
    * @Date   2022/1/18
    */
    @RequestMapping(value = "/resource-info")
    @ResponseBody
    public ResourceInfo getResourceInfo() {
        ResourceInfo backHtml = new ResourceInfo();
        try {
            backHtml.setMemTotal(GetSysInfoUtil.MemTotal());
            backHtml.setDiskTotal(GetSysInfoUtil.DiskTotal());
            backHtml.setQueryCount(CalStateUtil.queryCount);
            backHtml.setIdCount(nodeService.queryNodeIdTotal());

            if (CalStateUtil.queryCount == 0) {
                backHtml.setQueryTimeout(0);
            } else {
                backHtml.setQueryTimeout((float) CalStateUtil.queryTimeout / CalStateUtil.queryCount);
            }

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
    * @Description 根据企业前缀获取该前缀下所有标识的解析排名
    * @Param  param
    * @return com.hust.nodecontroller.infostruct.IdentityRankInfo
    * @Author jokerwen666
    * @Date   2022/1/18
    */
    @RequestMapping(value = "/identityRank")
    @ResponseBody
    public IdentityRankInfo queryIdentityRank(@RequestBody JSONObject param) {
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
    public NormalMsg bulkRegister(@RequestBody BulkRegister bulkRegister) {
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
}
