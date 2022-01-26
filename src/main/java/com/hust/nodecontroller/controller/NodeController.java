package com.hust.nodecontroller.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.hust.nodecontroller.exception.ControlSubSystemException;
import com.hust.nodecontroller.infostruct.answerstruct.*;
import com.hust.nodecontroller.infostruct.requestrequest.*;
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
    * 控制子系统标识注册接口
    * @param  registerIdRequest 标识注册请求信息
    * @return com.hust.nodecontroller.infostruct.AnswerStruct.NormalMsg
    * @Author jokerwen666
    * @Date   2022/1/20
    */
    @SentinelResource("DHT Register")
    @RequestMapping(value = "/register")
    @ResponseBody
    public NormalAnswer register(@RequestBody(required = false) RegisterIdRequest registerIdRequest) {
        NormalAnswer backHtml = new NormalAnswer();
        try {
            threadNum.addAndGet(1);
            nodeService.register(registerIdRequest);
            backHtml.setStatus(1);
            backHtml.setMessage("注册标识信息成功！");
            threadNum.decrementAndGet();
            return backHtml;

        } catch (ControlSubSystemException e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            threadNum.decrementAndGet();
            return backHtml;
        }
    }

    /**
     * 控制子系统批量注册接口
     * @param  bulkRegisterRequest 批量注册请求信息
     * @return com.hust.nodecontroller.infostruct.answerstruct.NormalAnswer
     * @Author jokerwen666
     * @Date   2022/1/21
     */
    @RequestMapping(value = "bulkRegister")
    @ResponseBody
    public NormalAnswer bulkRegister(@RequestBody BulkRegisterRequest bulkRegisterRequest) {
        NormalAnswer backHtml = new NormalAnswer();
        int idCount = bulkRegisterRequest.getData().size();
        CalStateUtil.registerCount = CalStateUtil.registerCount + idCount;
        CalStateUtil.totalCount = CalStateUtil.totalCount + idCount;
        try {
            threadNum.addAndGet(idCount);
            idCount = nodeService.bulkRegister(bulkRegisterRequest);
            backHtml.setStatus(1);
            backHtml.setMessage("批量注册标识信息成功!已成功注册" + idCount + "个标识");
            threadNum.decrementAndGet();
            return backHtml;

        } catch (ControlSubSystemException e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            threadNum.decrementAndGet();
            return backHtml;
        }
    }

    /**
    * 控制子系统标识删除接口
    * @param  deleteIdRequest 标识删除请求信息
    * @return com.hust.nodecontroller.infostruct.AnswerStruct.NormalMsg
    * @Author jokerwen666
    * @Date   2022/1/20
    */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public NormalAnswer delete(@RequestBody(required = false) DeleteIdRequest deleteIdRequest) {
        NormalAnswer backHtml = new NormalAnswer();
        try {
            threadNum.addAndGet(1);
            nodeService.delete(deleteIdRequest);
            backHtml.setStatus(1);
            backHtml.setMessage("删除标识信息成功！");
            threadNum.decrementAndGet();
            return backHtml;

        } catch (ControlSubSystemException e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            threadNum.decrementAndGet();
            return backHtml;
        }
    }

    /**
    * 控制子系统标识更新接口
    * @param  updateIdRequest 标识更新请求信息
    * @return com.hust.nodecontroller.infostruct.AnswerStruct.NormalMsg
    * @Author jokerwen666
    * @Date   2022/1/20
    */
    @RequestMapping(value = "/update")
    @ResponseBody
    public NormalAnswer update(@RequestBody UpdateIdRequest updateIdRequest) {
        NormalAnswer backHtml = new NormalAnswer();
        try {
            threadNum.addAndGet(1);
            nodeService.update(updateIdRequest);
            backHtml.setStatus(1);
            backHtml.setMessage("更新标识信息成功！");
            threadNum.decrementAndGet();
            return backHtml;

        } catch (ControlSubSystemException e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            threadNum.decrementAndGet();
            return backHtml;
        }
    }

    /**
    * 控制子系统标识解析接口
    * @param  queryIdRequest 标识解析请求信息
    * @return com.hust.nodecontroller.infostruct.QueryResult
    * @Author jokerwen666
    * @Date   2022/1/20
    */
    @RequestMapping(value = "/query")
    @ResponseBody
    public QueryIdAnswer query(@RequestBody QueryIdRequest queryIdRequest) {
        QueryIdAnswer backHtml = new QueryIdAnswer();
        try {
            threadNum.addAndGet(1);
            backHtml = nodeService.multipleTypeQuery(queryIdRequest,false);
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
    * 控制子系统DNS解析请求接口
    * @param  queryIdRequest 标识解析请求信息
    * @return com.hust.nodecontroller.infostruct.QueryResult
    * @Author jokerwen666
    * @Date   2022/1/20
    */
    @RequestMapping(value = "/dnsQuery")
    @ResponseBody
    public QueryIdAnswer dnsQuery(@RequestBody QueryIdRequest queryIdRequest) {
        QueryIdAnswer backHtml = new QueryIdAnswer();
        try {
            threadNum.addAndGet(1);
            backHtml = nodeService.multipleTypeQuery(queryIdRequest, true);
            backHtml.setStatus(1);
            backHtml.setMessage("DNS解析成功！");
            CalStateUtil.successCount++;
            threadNum.decrementAndGet();
            return backHtml;
        } catch (ControlSubSystemException e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            threadNum.decrementAndGet();
            return backHtml;
        }
    }

    /**
    * 控制子系统查找企业前缀下所有标识信息接口
    * @param  queryAllByPrefixRequest 查找企业前缀所有标识信息
    * @return com.hust.nodecontroller.infostruct.AnswerStruct.AllPrefixIdAnswer
    * @Author jokerwen666
    * @Date   2022/1/21
    */
    @RequestMapping(value = "/queryAllByPrefix")
    @ResponseBody
    public QueryAllByPrefixAnswer queryAllByPrefix(@RequestBody QueryAllByPrefixRequest queryAllByPrefixRequest) {
        QueryAllByPrefixAnswer backHtml = new QueryAllByPrefixAnswer();
        try{
            backHtml = nodeService.queryAllByPrefix(queryAllByPrefixRequest);
            return backHtml;
        }catch (ControlSubSystemException e){
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            return backHtml;
        }
    }

    /**
    * 控制子系统查询企业前缀下所有标识的解析排名接口
    * @param  queryIdentityRankRequest 查询标识解析排名请求信息
    * @return com.hust.nodecontroller.infostruct.AnswerStruct.QueryIdentityRankAnswer
    * @Author jokerwen666
    * @Date   2022/1/21
    */
    @RequestMapping(value = "/identityRank")
    @ResponseBody
    public QueryIdentityRankAnswer queryIdentityRank(@RequestBody QueryIdentityRankRequest queryIdentityRankRequest) {
        try {
            String prefix = queryIdentityRankRequest.getOrgPrefix();
            return nodeService.queryIdRankByPrefix(prefix);
        }catch (ControlSubSystemException e) {
            QueryIdentityRankAnswer backHtml = new QueryIdentityRankAnswer();
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            return backHtml;
        }
    }
    
    /**
    * 控制子系统获取系统信息接口（CPU使用率、内存使用率）
    * @return com.hust.nodecontroller.infostruct.answerstruct.GetSystemInfoAnswer
    * @Author jokerwen666
    * @Date   2022/1/21
    */
    @RequestMapping(value = "/system-info")
    @ResponseBody
    public GetSystemInfoAnswer getSystemInfo() {
        GetSystemInfoAnswer backHtml = new GetSystemInfoAnswer();
        backHtml.setData(GetSysInfoUtil.getMinuteSysInfo());
        backHtml.setStatus(1);
        backHtml.setMessage("Success!");
        return backHtml;
    }
    
    /**
    * 控制子系统获取运行时信息接口
    * @return com.hust.nodecontroller.infostruct.answerstruct.GetRuntimeInfoAnswer
    * @Author jokerwen666
    * @Date   2022/1/21
    */
    @RequestMapping(value = "/runtime-info1")
    @ResponseBody
    public GetRuntimeInfoAnswer getRuntimeInfo1() {
        GetRuntimeInfoAnswer backHtml= new GetRuntimeInfoAnswer();
        backHtml.setData(CalStateUtil.getRuntimeInfoList1());
        backHtml.setStatus(1);
        backHtml.setMessage("Success!");
        return backHtml;
    }

    /**
     * 控制子系统获取运行时信息接口
     * @return com.hust.nodecontroller.infostruct.answerstruct.GetRuntimeInfoAnswer
     * @Author jokerwen666
     * @Date   2022/1/21
     */
    @RequestMapping(value = "/runtime-info2")
    @ResponseBody
    public GetRuntimeInfoAnswer getRuntimeInfo2() {
        GetRuntimeInfoAnswer backHtml= new GetRuntimeInfoAnswer();
        backHtml.setData(CalStateUtil.getRuntimeInfoList2());
        backHtml.setStatus(1);
        backHtml.setMessage("Success!");
        return backHtml;

    }
    
    /**
    * 控制子系统获取节点资源信息接口
    * @return com.hust.nodecontroller.infostruct.answerstruct.GetResourceInfoAnswer
    * @Author jokerwen666
    * @Date   2022/1/21
    */
    @RequestMapping(value = "/resource-info")
    @ResponseBody
    public GetResourceInfoAnswer getResourceInfo() {
        GetResourceInfoAnswer backHtml = new GetResourceInfoAnswer();
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
        }catch (ControlSubSystemException e) {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
            return backHtml;
        }

    }

    /**
    * 控制子系统获取异构标识解析信息接口
    * @return com.hust.nodecontroller.infostruct.answerstruct.GetMultipleIdQueryInfoAnswer
    * @Author jokerwen666
    * @Date   2022/1/21
    */
    @RequestMapping(value = "/hidInfo")
    @ResponseBody
    public GetMultipleIdQueryInfoAnswer queryHidInfo() {
        GetMultipleIdQueryInfoAnswer backHtml = new GetMultipleIdQueryInfoAnswer();
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

    /**
    * 控制子系统获取行业解析信息接口
    * @return com.hust.nodecontroller.infostruct.answerstruct.GetIndustryIdQueryInfoAnswer
    * @Author jokerwen666
    * @Date   2022/1/21
    */
    @RequestMapping(value = "/industryInfo")
    @ResponseBody
    public GetIndustryIdQueryInfoAnswer queryIndustryInfo() {
        GetIndustryIdQueryInfoAnswer backHtml = new GetIndustryIdQueryInfoAnswer();
        backHtml.setIndustryName("086.001");
        backHtml.setDataCount(IndustryQueryUtil.calIndustryQueryInfo());
        backHtml.setStatus(1);
        backHtml.setMessage("行业时段解析查询成功！");
        return backHtml;
    }
}
