package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.context.event.SpringApplicationEvent;

import java.util.List;

public class BulkRegister {
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
