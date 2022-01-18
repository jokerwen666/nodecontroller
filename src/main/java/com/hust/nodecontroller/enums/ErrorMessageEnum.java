package com.hust.nodecontroller.enums;



public enum ErrorMessageEnum {
    /** 当接收到解析请求对参数解密失败时抛出*/
    QUERY_DECRYPT_ERROR("签名检验失败！请求方存在安全风险"),

    /** 当进行操作权限校验失败时抛出 */
    OPERATION_AUTHORITY_VERIFY_ERROR("操作权限检验失败！请求方无请求的操作权限！"),

    /** 解析标识时如果DHT节点上存储的url与区块链上存储的url不相等时抛出 */
    URLHASH_VERIFY_ERROR("标识对应的URL可能被篡改！"),

    /** 解析标识时如果企业节点上存储的产品信息与区块链上存储的产品信息不相等时抛出 */
    GOODSHASH_VERIFY_ERROR("标识对应的产品信息可能被篡改！");

    private String msg;


    ErrorMessageEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
