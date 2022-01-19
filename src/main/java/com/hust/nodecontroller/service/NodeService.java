package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.controller.NodeController;
import com.hust.nodecontroller.infostruct.*;

import java.util.concurrent.Future;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName NodeService
 * @date 2020.09.20 17:01
 */
public interface NodeService {
    /**
     * 通过控制节点进行标识注册
     * @param infoFromClient
     * @throws Exception
     */
    void register(InfoFromClient infoFromClient) throws Exception;

    /**
     * 通过控制节点进行标识删除
     * @param infoFromClient
     * @throws Exception
     */
    void delete(InfoFromClient infoFromClient) throws Exception;

    /**
     * 通过控制节点进行标识更新
     * @param infoFromClient
     * @throws Exception
     */
    void update(InfoFromClient infoFromClient) throws Exception;

    /**
     * 通过控制节点获取系统中企业节点总数
     * @return int
     * @throws Exception
     */
    int queryNodeIdTotal() throws Exception;

    /**
     * 通过控制节点进行标识查询
     * @param infoFromClient
     * @return QueryResult
     * @throws Exception
     */
    QueryResult query(InfoFromClient infoFromClient) throws Exception;

    QueryResult multipleTypeQuery(InfoFromClient infoFromClient, boolean isDnsQuery) throws Exception;

    /**
     * 通过控制节点获取全部企业节点信息
     * @return NodeState
     * @throws Exception
     */
    NodeState queryNodeState() throws Exception;

    /**
     * 通过控制节点获取整个标识解析系统的部分系统信息（解析总量、企业节点总数、行业总数）
     * @return SystemTotalState
     * @throws Exception
     */
    SystemTotalState querySystemTotalState() throws Exception;

    /**
     * 通过控制节点查询某企业前缀下（也即某企业拥有的）全部标识信息
     * @return SystemTotalState
     * @throws Exception
     */
    IdentityInfo queryAllByPrefix(InfoFromClient infoFromClient) throws Exception;

    /**
     * 通过控制节点获取某企业前缀下（也即某企业拥有的）全部标识排行（解析次数排行）
     * @param prefix
     * @return
     * @throws Exception
     */
    public IdentityRankInfo queryIdRankByPrefix(String prefix) throws Exception;

    /**
     * 批量注册标识
     * @param bulkRegister
     * @return
     * @throws Exception
     */
    int bulkRegister(BulkRegister bulkRegister) throws Exception;

    /**
     * 通过控制节点进行批量查询
     * @param jsonArray
     * @return
     */
    BulkInfo bulkQuery(JSONArray jsonArray);

}
