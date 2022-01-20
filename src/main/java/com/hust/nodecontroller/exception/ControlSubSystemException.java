package com.hust.nodecontroller.exception;

import com.hust.nodecontroller.enums.ErrorMessageEnum;

/**
 * @program nodecontroller
 * @Description 控制子系统异常类
 * @Author jokerwen666
 * @date 2022-01-20 22:18
 **/

public class ControlSubSystemException extends Exception {
    public ControlSubSystemException (String message) {
        super(message);
    }

}
