package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.AnswerStruct.NormalMsg;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName SystemState
 * @date 2020.10.17 19:12
 */

@Component
public class SystemState extends NormalMsg {
    List<JSONObject> data;

    public List<JSONObject> getData() {
        return data;
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
    }
}
