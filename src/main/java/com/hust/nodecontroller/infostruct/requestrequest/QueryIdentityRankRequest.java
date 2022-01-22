package com.hust.nodecontroller.infostruct.requestrequest;

/**
 * @program nodecontroller
 * @Description 查询企业前缀下所有标识的解析排名请求
 * @Author jokerwen666
 * @date 2022-01-21 17:22
 **/

public class QueryIdentityRankRequest extends ClientRequest {
    private String orgPrefix;


    public String getOrgPrefix() {
        return orgPrefix;
    }

    public void setOrgPrefix(String orgPrefix) {
        this.orgPrefix = orgPrefix;
    }
}
