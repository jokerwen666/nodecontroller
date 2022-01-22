package com.hust.nodecontroller.infostruct.requestrequest;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class BulkRegisterRequest {
    private String client;
    private List<JSONObject> data;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public List<JSONObject> getData() {
        return data;
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
    }
}
