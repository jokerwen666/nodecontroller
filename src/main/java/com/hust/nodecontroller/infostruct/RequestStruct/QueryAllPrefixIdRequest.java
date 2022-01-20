package com.hust.nodecontroller.infostruct.RequestStruct;

/**
 * @program nodecontroller
 * @Description 查询企业前缀下所有标识请求
 * @Author jokerwen666
 * @date 2022-01-20 23:07
 **/
public class QueryAllPrefixIdRequest extends ClientRequest {
    private String orgPrefix;
    private String matchString;

    public String getOrgPrefix() {
        return orgPrefix;
    }

    public void setOrgPrefix(String orgPrefix) {
        this.orgPrefix = orgPrefix;
    }

    public String getMatchString() {
        return matchString;
    }

    public void setMatchString(String matchString) {
        this.matchString = matchString;
    }
}
