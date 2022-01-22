package com.hust.nodecontroller.service;

import com.hust.nodecontroller.exception.ControlSubSystemException;
import com.hust.nodecontroller.infostruct.answerstruct.QueryIdentityRankAnswer;
import com.hust.nodecontroller.infostruct.answerstruct.QueryAllByPrefixAnswer;
import com.hust.nodecontroller.infostruct.answerstruct.QueryIdAnswer;
import com.hust.nodecontroller.infostruct.requestrequest.*;

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
    QueryIdAnswer multipleTypeQuery(QueryIdRequest queryIdRequest, boolean isDnsQuery) throws ControlSubSystemException;

    /**
     * 通过控制节点查询某企业前缀下（也即某企业拥有的）全部标识信息
     * @return SystemTotalState
     * @throws Exception
     */
    QueryAllByPrefixAnswer queryAllByPrefix(QueryAllByPrefixRequest queryAllByPrefixRequest) throws ControlSubSystemException;


    /**
     * 通过控制节点获取系统中企业节点总数
     * @return int
     * @throws Exception
     */
    int queryNodeIdTotal() throws ControlSubSystemException;

    /**
     * 通过控制节点获取某企业前缀下（也即某企业拥有的）全部标识排行（解析次数排行）
     * @param prefix
     * @return
     * @throws Exception
     */
    QueryIdentityRankAnswer queryIdRankByPrefix(String prefix) throws ControlSubSystemException;

    /**
     * 批量注册标识
     * @param bulkRegisterRequest
     * @return
     * @throws Exception
     */
    int bulkRegister(BulkRegisterRequest bulkRegisterRequest) throws ControlSubSystemException;
}
