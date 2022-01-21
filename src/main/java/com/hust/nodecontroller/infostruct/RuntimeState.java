package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.AnswerStruct.NormalAnswer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName RuntimeState
 * @date 2020.10.17 18:21
 */
@Component
public class RuntimeState extends NormalAnswer {
    private List<JSONObject> data;

    public List<JSONObject> getData() {
        return data;
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
    }
}