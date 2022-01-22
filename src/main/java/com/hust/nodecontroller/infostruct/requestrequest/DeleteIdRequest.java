package com.hust.nodecontroller.infostruct.requestrequest;

import com.hust.nodecontroller.enums.ErrorMessageEnum;
import com.hust.nodecontroller.exception.ControlSubSystemException;

/**
 * @program nodecontroller
 * @Description 删除标识请求类
 * @Author jokerwen666
 * @date 2022-01-20 22:05
 **/

public class DeleteIdRequest extends ClientRequest {
    private String identification;

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
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
