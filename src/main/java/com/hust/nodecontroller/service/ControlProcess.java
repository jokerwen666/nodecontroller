package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.*;

/**
 * @program nodecontroller
 * @Description
 * @Author jokerwen666
 * @Date  2022-01-18 19:28
 **/
public interface ControlProcess {
    /**
    * 处理企业操作（增、删、改）
    * @param  client 请求用户
    * @param  identity 标识
    * @param  prefix 标识前缀
    * @param  data 标识数据
    * @param  dhtUrl dht节点url
    * @param  bcUrl 区块链节点url
    * @param  type 操作类型
    * @return void
    * @throws Exception 企业操作可能抛出的异常
    * @Author jokerwen666
    * @Date   2022/1/18
    */
    void enterpriseHandle(String client, String identity, String prefix, JSONObject data, String dhtUrl, String bcUrl, int type) throws Exception;

    QueryResult userHandle(String identity, String client, String dhtUrl, String bcUrl, String bcQueryOwner) throws Exception;

     /** */
    IdentityInfo identityHandle(InfoFromClient infoFromClient, String bcUrl) throws Exception;

    /** */
    int bulkRegister(BulkRegister bulkRegister, String dhtUrl, String bcUrl) throws Exception;

    /** */
    BulkInfo bulkQuery(JSONArray jsonArray, String url) ;
}
