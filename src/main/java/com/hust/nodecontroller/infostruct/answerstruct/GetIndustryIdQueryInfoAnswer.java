package com.hust.nodecontroller.infostruct.answerstruct;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class GetIndustryIdQueryInfoAnswer extends NormalAnswer {
    private String industryName;
    private List<JSONObject> dataCount;


    public List<JSONObject> getDataCount() {
        return dataCount;
    }

    public void setDataCount(List<JSONObject> dataCount) {
        this.dataCount = dataCount;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }
}
