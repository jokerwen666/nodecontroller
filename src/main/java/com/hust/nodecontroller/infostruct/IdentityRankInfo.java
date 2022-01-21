package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.AnswerStruct.NormalAnswer;

import java.util.List;

public class IdentityRankInfo extends NormalAnswer {
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
