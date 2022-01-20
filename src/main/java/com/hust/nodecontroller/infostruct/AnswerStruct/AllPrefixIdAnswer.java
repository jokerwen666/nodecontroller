package com.hust.nodecontroller.infostruct.AnswerStruct;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @program nodecontroller
 * @Description 企业前缀下所有标识
 * @Author jokerwen666
 * @date 2022-01-20 23:39
 **/

public class AllPrefixIdAnswer extends NormalMsg {
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
