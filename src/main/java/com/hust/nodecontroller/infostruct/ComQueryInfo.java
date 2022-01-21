package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.AnswerStruct.NormalAnswer;
import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName ComQueryInfo
 * @date 2020.10.20 18:10
 */

@Component
public class ComQueryInfo extends NormalAnswer {
    private JSONObject information;
    private String jsonStr;

    public JSONObject getInformation() {
        return information;
    }

    public void setInformation(JSONObject information) {
        this.information = information;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }
}
