package com.hust.nodecontroller.infostruct.AnswerStruct;

import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description
 * 该类用于存储从标识管理子系统处获得的查询结果
 *
 * @ClassName IMSystemInfo
 * @date 2020.10.12 17:38
 */

/**
 * @program nodecontroller
 * @Description 标识管理子系统响应类
 * @Author jokerwen666
 * @date 2022-01-20 23:39
 **/
@Component
public class IdentityManagementSystemAnswer extends NormalAnswer {
    String mappingData;
    String nodeID;

    public String getMappingData() {
        return mappingData;
    }

    public void setMappingData(String mappingData) {
        this.mappingData = mappingData;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }
}
