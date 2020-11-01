package com.hust.nodecontroller.infostruct;

import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description
 * 该类用于存储从解析结果验证子系统中获得的查询结果
 *
 * @ClassName RVSystemInfo
 * @date 2020.10.12 17:40
 */

@Component
public class RVSystemInfo extends NormalMsg{
    private String urlHash;
    private String mappingDataHash;

    public String getUrlHash() {
        return urlHash;
    }

    public void setUrlHash(String urlHash) {
        this.urlHash = urlHash;
    }

    public String getMappingDataHash() {
        return mappingDataHash;
    }

    public void setMappingDataHash(String mappingDataHash) {
        this.mappingDataHash = mappingDataHash;
    }
}
