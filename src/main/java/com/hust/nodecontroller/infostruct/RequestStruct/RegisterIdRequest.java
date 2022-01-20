package com.hust.nodecontroller.infostruct.RequestStruct;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.enums.ErrorMessageEnum;
import com.hust.nodecontroller.exception.ControlSubSystemException;

/**
 * @program nodecontroller
 * @Description 标识注册信息类
 * @Author jokerwen666
 * @date 2022-01-20 21:52
 **/

public class RegisterIdRequest extends ClientRequest {
    private String identification;
    private JSONObject data;

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
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
}
