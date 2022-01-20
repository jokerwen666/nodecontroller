package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.AnswerStruct.NormalMsg;

import java.util.List;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName NodeState
 * @date 2020.10.28 18:16
 */
public class NodeState extends NormalMsg {
    int nodeCount;

    int boundaryID;
    String domainID;
    List<JSONObject> nodeList;

    public int getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public int getBoundaryID() {
        return boundaryID;
    }

    public void setBoundaryID(int boundaryID) {
        this.boundaryID = boundaryID;
    }

    public String getDomainID() {
        return domainID;
    }

    public void setDomainID(String domainID) {
        this.domainID = domainID;
    }

    public List<JSONObject> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<JSONObject> nodeList) {
        this.nodeList = nodeList;
    }
}
