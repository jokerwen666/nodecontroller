package com.hust.nodecontroller.infostruct.AnswerStruct;

import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description
 * 该类用于存储从权限管理子系统获得的json结果
 *
 * @ClassName AMSystemInfo
 * @date 2020.10.05 16:43
 */

/**
 * @program nodecontroller
 * @Description 权限管理子系统响应类
 * @Author jokerwen666
 * @date 2022-01-20 23:39
 **/
@Component
public class AuthorityManagementSystemAnswer extends NormalAnswer {
    private String prefix;
    private String authority;
    private String owner;
    private String org_name;
    private String key;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}