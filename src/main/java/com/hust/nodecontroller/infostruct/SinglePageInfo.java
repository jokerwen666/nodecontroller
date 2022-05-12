package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class SinglePageInfo extends NormalMsg{
    int count;
    String txid;
    List<JSONObject> identityList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public List<JSONObject> getIdentityList() {
        return identityList;
    }

    public void setIdentityList(List<JSONObject> identityList) {
        this.identityList = identityList;
    }
}
