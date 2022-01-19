package com.hust.nodecontroller.exception;

import com.hust.nodecontroller.enums.ErrorMessageEnum;

/**
 * @program nodecontroller
 * @Description 记录控制子系统中的异常
 * @Author jokerwen666
 * @date 2022-01-19 00:29
 **/

public class ControlSubSystemException extends Exception{
    private final ErrorMessageEnum errorMessageEnum;

    public ControlSubSystemException(ErrorMessageEnum errorMessageEnum) {
        this.errorMessageEnum = errorMessageEnum;
    }

    @Override
    public String getMessage() {
        return errorMessageEnum.getMsg();
    }
}
