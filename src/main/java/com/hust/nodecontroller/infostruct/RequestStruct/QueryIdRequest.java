package com.hust.nodecontroller.infostruct.RequestStruct;

import com.hust.nodecontroller.enums.ErrorMessageEnum;
import com.hust.nodecontroller.exception.ControlSubSystemException;

/**
 * @program nodecontroller
 * @Description 解析标识请求类
 * @Author jokerwen666
 * @date 2022-01-20 22:06
 **/

public class QueryIdRequest extends ClientRequest {
    private String identification;
    private String type;

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrefix() throws ControlSubSystemException {
        String[] idList = identification.split("/");
        int idPartNum = 2;
        if (idList.length != idPartNum) {
            throw new ControlSubSystemException(ErrorMessageEnum.IDENTIFICATION_PREFIX_SPLIT_ERROR.getMsg());
        }

        else {
            String prefix = idList[0];
            String suffix = idList[1];
            return prefix;
        }
    }

    public  String getDomainPrefix() throws ControlSubSystemException {
        String prefix = getPrefix();
        int pos = prefix.lastIndexOf(".");
        if (pos == -1) {
            throw new ControlSubSystemException(ErrorMessageEnum.IDENTIFICATION_DOMAIN_PREFIX_ERROR.getMsg());
        }

        return prefix.substring(0,pos);
    }
}
