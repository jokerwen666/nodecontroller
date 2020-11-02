package com.hust.nodecontroller.infostruct;

import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName ComQueryInfo
 * @date 2020.10.20 18:10
 */

@Component
public class ComQueryInfo extends NormalMsg{
    private String information;
    private String jsonStr;

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }
}
