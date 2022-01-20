package com.hust.nodecontroller.infostruct.AnswerStruct;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName IdentityInfo
 * @date 2020.10.30 17:21
 */
public class IdentityInfo extends NormalMsg {
    int totalCount;
    int pageCount;
    List<JSONObject> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<JSONObject> getData() {
        return data;
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
    }
}
