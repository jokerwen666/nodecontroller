package com.hust.nodecontroller.infostruct.answerstruct;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class QueryIdentityRankAnswer extends NormalAnswer {
    private List<JSONObject> idList;
    private int idNums;

    public List<JSONObject> getIdList() {
        return idList;
    }

    public void setIdList(List<JSONObject> idList) {
        this.idList = idList;
    }

    public int getIdNums() {
        return idNums;
    }

    public void setIdNums(int idNums) {
        this.idNums = idNums;
    }
}
