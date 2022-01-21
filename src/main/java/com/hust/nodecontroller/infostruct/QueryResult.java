package com.hust.nodecontroller.infostruct;

import com.hust.nodecontroller.infostruct.AnswerStruct.NormalAnswer;
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
public class QueryResult extends NormalAnswer {
    private String url;
    private String nodeID;
    private String goodsInfo;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }
}
