package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONArray;
import com.hust.nodecontroller.exception.ControlSubSystemException;
import com.hust.nodecontroller.infostruct.*;
import com.hust.nodecontroller.infostruct.AnswerStruct.AllPrefixIdAnswer;
import com.hust.nodecontroller.infostruct.AnswerStruct.IdentityInfo;
import com.hust.nodecontroller.infostruct.RequestStruct.*;

import java.util.concurrent.ExecutionException;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName NodeService
 * @date 2020.09.20 17:01
 */
public interface NodeService {
    /**
    * 标识注册
    * @param  registerIdRequest 标识注册请求信息
    * @return void
    * @throws ControlSubSystemException 控制子系统异常
    * @Author jokerwen666
    * @Date   2022/1/20
    */
    void register(RegisterIdRequest registerIdRequest) throws ControlSubSystemException;

    /**
    * 标识删除
    * @param  deleteIdRequest 标识删除请求信息
    * @return void
    * @throws ControlSubSystemException 控制子系统异常
    * @Author jokerwen666
    * @Date   2022/1/20
    */
    void delete(DeleteIdRequest deleteIdRequest) throws ControlSubSystemException;

    /**
    * 标识更新
    * @param  updateIdRequest 标识更新请求信息
    * @return void
    * @throws ControlSubSystemException 控制子系统异常
    * @Author jokerwen666
    * @Date   2022/1/20
    */
    void update(UpdateIdRequest updateIdRequest) throws ControlSubSystemException;

    /**
    * 异构标识解析
    * @param  queryIdRequest 标识解析请求信息
    * @param  isDnsQuery 解析请求是否为DNS解析
    * @return com.hust.nodecontroller.infostruct.QueryResult
    * @throws ControlSubSystemException 控制子系统异常
    * @Author jokerwen666
    * @Date   2022/1/20
    */
    QueryResult multipleTypeQuery(QueryIdRequest queryIdRequest, boolean isDnsQuery) throws ControlSubSystemException;

    /**
     * 通过控制节点查询某企业前缀下（也即某企业拥有的）全部标识信息
     * @return SystemTotalState
     * @throws Exception
     */
    AllPrefixIdAnswer queryAllByPrefix(QueryAllPrefixIdRequest queryAllPrefixIdRequest) throws ControlSubSystemException;


    /**
     * 通过控制节点获取系统中企业节点总数
     * @return int
     * @throws Exception
     */
    int queryNodeIdTotal() throws Exception;

    /**
     * 通过控制节点获取某企业前缀下（也即某企业拥有的）全部标识排行（解析次数排行）
     * @param prefix
     * @return
     * @throws Exception
     */
    IdentityRankInfo queryIdRankByPrefix(String prefix) throws Exception;

    /**
     * 批量注册标识
     * @param bulkRegister
     * @return
     * @throws Exception
     */
    int bulkRegister(BulkRegister bulkRegister) throws Exception;
}
