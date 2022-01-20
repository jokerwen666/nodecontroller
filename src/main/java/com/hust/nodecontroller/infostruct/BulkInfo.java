package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONArray;
import com.hust.nodecontroller.infostruct.AnswerStruct.NormalMsg;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName BulkInfo
 * @date 2020.12.01 16:39
 */
public class BulkInfo extends NormalMsg {
    private String beginTime;
    private String endTime;
    private String costTime;
    private String rate;
    private JSONArray data;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }
}
