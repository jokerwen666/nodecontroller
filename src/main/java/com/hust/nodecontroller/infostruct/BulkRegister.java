package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.context.event.SpringApplicationEvent;

import java.util.List;

public class BulkRegister {
    private String client;
    private JSONArray data;
    private String prefix;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
