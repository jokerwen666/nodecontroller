package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description
 * 该类用于存储给前端返回的查询结果
 *
 * @ClassName QueryResult
 * @date 2020.09.20 17:03
 */

@Component
public class QueryResult extends NormalMsg{
    private String url;
    private JSONObject goodsInfo;
    private String nodeID;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JSONObject getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(JSONObject goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }
}
