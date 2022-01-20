package com.hust.nodecontroller.infostruct;

import com.hust.nodecontroller.infostruct.AnswerStruct.NormalMsg;
import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description
 * 该类用于存储从标识管理子系统处获得的查询结果
 *
 * @ClassName IMSystemInfo
 * @date 2020.10.12 17:38
 */

@Component
public class IMSystemInfo extends NormalMsg {
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
