package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class HidInfo extends NormalMsg{
    private List<JSONObject> data;

    public List<JSONObject> getData() {
        return data;
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
    }
}
