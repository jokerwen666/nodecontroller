package com.hust.nodecontroller.service;

import com.hust.nodecontroller.infostruct.IdentityInfo;
import com.hust.nodecontroller.infostruct.InfoFromClient;
import com.hust.nodecontroller.infostruct.QueryResult;

public interface ControlProcess {

    public void enterpriseHandle(InfoFromClient infoFromClient, String dhtUrl, String bcUrl, int type) throws Exception;

    public QueryResult userHandle(InfoFromClient infoFromClient, String dhtUrl, String bcUrl) throws Exception;

    public IdentityInfo identityHandle(InfoFromClient infoFromClient, String bcUrl) throws Exception;
}
