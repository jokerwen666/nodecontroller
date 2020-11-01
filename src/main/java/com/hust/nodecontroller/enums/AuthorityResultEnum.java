package com.hust.nodecontroller.enums;

/**
 * @author Zhang Bowen
 * @Description
 * 该枚举类主要用于列举控制子系统中权限校验相关的异常
 *
 * @ClassName AuthorityResultEnum
 * @date 2020.09.13 14:36
 */


public enum AuthorityResultEnum {
    SIGNATURE_VERIFY_ERROR("签名检验失败！请求方存在安全风险"),
    OPERATION_AUTHORITY_VERIFY_ERROR("操作权限检验失败！请求方无请求的操作权限！"),
    URLHASH_VERIFY_ERROR("标识对应的URL可能被篡改！"),
    GOODSHASH_VERIFY_ERROR("标识对应的产品信息可能被篡改！");


    private String msg;

    AuthorityResultEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
