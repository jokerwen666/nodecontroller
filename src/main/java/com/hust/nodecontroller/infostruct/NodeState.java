package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName NodeState
 * @date 2020.10.28 18:16
 */
public class NodeState extends NormalMsg{
    int nodeCount;
    int domainID;
    int boundaryID;
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

    public int getDomainID() {
        return domainID;
    }

    public void setDomainID(int domainID) {
        this.domainID = domainID;
    }

    public List<JSONObject> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<JSONObject> nodeList) {
        this.nodeList = nodeList;
    }
}
