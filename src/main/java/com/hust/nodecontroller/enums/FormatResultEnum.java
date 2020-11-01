package com.hust.nodecontroller.enums;

/**
 * @author Zhang Bowen
 * @Description
 * 该枚举类主要用于列举格式化过程中的异常类型
 *
 * @ClassName FormatResultEnum
 * @date 2020.09.13 15:35
 */


public enum  FormatResultEnum {
    IDENTIFICATION_PREFIX_SPLIT_ERROR("标识格式错误！无法获取标识前缀信息！"),
    BLOCKCHAIN_FORMAT_ERROR("从区块链中获得的企业信息格式错误！无法获取企业信息！"),
    QUERY_FORMAT_ERROR("查询操作输入格式错误！输入了除标识外的多余信息！");

    private String msg;

    FormatResultEnum() {}

    FormatResultEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
