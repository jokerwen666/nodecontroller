package com.hust.nodecontroller.enums;

/**
 * @program nodecontroller
 * @Description 加解密方式枚举类
 * @Author jokerwen666
 * @date 2022-01-20 19:48
 **/
public enum EncryptTypeEnum {
    /** 使用RSA算法进行加解密*/
    ENCRYPT_TYPE_RSA(1, "rsa"),

    /** 使用SM2国密算法加解密*/
    ENCRYPT_TYPE_SM2(2, "sm2"),

    /** 不使用任何加解密算法*/
    ENCRYPT_TYPE_NONE(0, "none");

    private int typeCode;
    private String typeString;

    EncryptTypeEnum(int typeCode, String typeString) {
        this.typeCode = typeCode;
        this.typeString = typeString;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }
}
