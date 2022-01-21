package com.hust.nodecontroller.infostruct.AnswerStruct;

import org.springframework.stereotype.Component;

/**
 * 该类主要用于接收简单的返回消息，形如<status,msg>
 * 其中status表示状态，1为成功，0为失败
 * @author Zhang Bowen
 * @ClassName NormalMsg
 * @date 2020.09.15 10:44
 */

@Component
public class NormalAnswer {

    private int status;
    private String message;

    public NormalAnswer(){};

    public NormalAnswer(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
