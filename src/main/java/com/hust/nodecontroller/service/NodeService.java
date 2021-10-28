package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONArray;
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
    void register(InfoFromClient infoFromClient) throws Exception;
    void delete(InfoFromClient infoFromClient) throws Exception;
    void update(InfoFromClient infoFromClient) throws Exception;
    int QueryNodeIdTotal() throws Exception;
    QueryResult query(InfoFromClient infoFromClient) throws Exception;
    NodeState queryNodeState() throws Exception;
    SystemTotalState querySystemTotalState() throws Exception;
    IdentityInfo queryAllByPrefix(InfoFromClient infoFromClient) throws Exception;
    BulkInfo bulkRegister(JSONArray jsonArray);
    BulkInfo bulkQuery(JSONArray jsonArray);

}
