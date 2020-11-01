package com.hust.nodecontroller.exception;

import com.hust.nodecontroller.enums.FormatResultEnum;

/**
 * @author Zhang Bowen
 * @Description
 * 该异常类用于记录格式化过程中的异常类型
 *
 * @ClassName FormatException
 * @date 2020.09.13 14:57
 */

public class FormatException extends Exception{
    private final FormatResultEnum formatResultEnum;

    public FormatException(FormatResultEnum formatResultEnum) {
        this.formatResultEnum = formatResultEnum;
    }

    public FormatException(String message, FormatResultEnum formatResultEnum) {
        super(message);
        this.formatResultEnum = formatResultEnum;
    }

    public FormatException(String message, Throwable cause, FormatResultEnum formatResultEnum) {
        super(message, cause);
        this.formatResultEnum = formatResultEnum;
    }

    public FormatException(Throwable cause, FormatResultEnum formatResultEnum) {
        super(cause);
        this.formatResultEnum = formatResultEnum;
    }

    public FormatResultEnum getFormatResultEnum() {
        return formatResultEnum;
    }
}
