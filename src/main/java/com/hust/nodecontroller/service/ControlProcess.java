package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONArray;
import com.hust.nodecontroller.infostruct.*;

public interface ControlProcess {

    public void enterpriseHandle(InfoFromClient infoFromClient, String dhtUrl, String bcUrl, int type) throws Exception;

    public QueryResult userHandle(String identity, String client, String dhtUrl, String bcUrl, String bcQueryOwner, String EncType) throws Exception;

    public IdentityInfo identityHandle(InfoFromClient infoFromClient, String bcUrl) throws Exception;

    public int bulkRegister(BulkRegister bulkRegister, String dhtUrl, String bcUrl) throws Exception;

    public BulkInfo bulkQuery(JSONArray jsonArray, String url) ;
}
