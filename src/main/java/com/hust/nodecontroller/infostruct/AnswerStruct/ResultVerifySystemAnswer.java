package com.hust.nodecontroller.infostruct.AnswerStruct;

import org.springframework.stereotype.Component;

/**
 * @program nodecontroller
 * @Description 结果验证子系统响应类
 * @Author jokerwen666
 * @date 2022-01-20 23:39
 **/
@Component
public class ResultVerifySystemAnswer extends NormalAnswer {
    private String urlHash;
    private String mappingDataHash;
    private String permission;
    private String owner;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

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
