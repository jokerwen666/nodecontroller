package com.hust.nodecontroller.enums;

/**
 * @program nodecontroller
 * @Description 请求类型枚举类
 * @Author jokerwen666
 * @date 2022-01-20 19:48
 **/
public enum RequestTypeEnum {
    /** OID类型标识 */
    REQUEST_TYPE_REGISTER(8, "注册标识请求"),

    /***/
    REQUEST_TYPE_DELETE(4, "删除标识请求"),

    /***/
    REQUEST_TYPE_UPDATE(2, "更新标识请求"),

    /***/
    REQUEST_TYPE_QUERY(1, "解析标识请求");

    private final int requestCode;
    private final String requestMessage;

    RequestTypeEnum(int requestCode, String requestMessage) {
        this.requestCode = requestCode;
        this.requestMessage = requestMessage;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public String getRequestMessage() {
        return requestMessage;
    }
}
