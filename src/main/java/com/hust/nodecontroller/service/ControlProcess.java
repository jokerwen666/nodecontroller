package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONArray;
import com.hust.nodecontroller.infostruct.BulkInfo;
import com.hust.nodecontroller.infostruct.IdentityInfo;
import com.hust.nodecontroller.infostruct.InfoFromClient;
import com.hust.nodecontroller.infostruct.QueryResult;

public interface ControlProcess {

    public void enterpriseHandle(InfoFromClient infoFromClient, String dhtUrl, String bcUrl, int type) throws Exception;

    public QueryResult userHandle(String identity, String client, String dhtUrl, String bcUrl, String bcQueryOwner) throws Exception;

    public IdentityInfo identityHandle(InfoFromClient infoFromClient, String bcUrl) throws Exception;

    public BulkInfo bulkRegister(JSONArray jsonArray, String dhtUrl, String bcUrl) ;

    public BulkInfo bulkQuery(JSONArray jsonArray, String url) ;
}
