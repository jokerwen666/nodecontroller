package com.hust.nodecontroller.enums;

/**
 * @program nodecontroller
 * @Description 标识类型枚举类
 * @Author jokerwen666
 * @date 2022-01-19 17:43
 **/
public enum IdentityTypeEnum {
    /** OID类型标识 */
    IDENTITY_TYPE_OID(1, "OID类型标识，分为4段，前3段一般为\"1.2.156\", \"2.13.156\""),

    /** Ecode标识类型 */
    IDENTITY_TYPE_ECODE(2, "Ecode类型标识，一般以\"10064\", \"10096\", \"20128\",\"300121\"开头"),

    /** Handle标识类型 */
    IDENTITY_TYPE_HANDLE(3, "Handle类型标识，分为3段，第1段一般为\"10\",\"11\",\"20\",\"21\",\"22\",\"25\",\"27\",\"44\",\"77\",\"86\""),

    /** 创新型标识类型 */
    IDENTITY_TYPE_DHT(4, "创新型标识，以\"\\\"分为两部分，前缀标识企业，后缀标识产品，前缀分为3段，后缀分为5段"),

    /** DNS域名解析类型*/
    IDENTITY_TYPE_DNS(5, "DNS待解析域名"),

    /** 不支持的标识类型*/
    IDENTITY_TYPE_NOT_SUPPORT(0, "该标识类型系统不支持，无法进行解析")
    ;

    private final int idCode;
    private final String idTypeMessage;


    IdentityTypeEnum(int idCode, String idTypeMessage) {
        this.idCode = idCode;
        this.idTypeMessage = idTypeMessage;
    }

    public int getIdCode() {
        return this.idCode;
    }

    public String getIdTypeMessage() {
        return this.idTypeMessage;
    }
}
