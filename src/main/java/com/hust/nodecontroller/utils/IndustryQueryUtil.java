package com.hust.nodecontroller.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class IndustryQueryUtil {
    public static List<JSONObject> dataCount = new LinkedList<>();
    public static ArrayList<Integer> tmpArray = new ArrayList<>();
    public static JSONObject periodData = new JSONObject();
    public static int queryCount = 0;
    public static int preQueryCount = 0;

    public static List<JSONObject> calIndustryQueryInfo() {
        long currentTime = new Date().getTime() / 1000;

        // 如果当前请求时刻不是整点，则将tmparray中数据临时添加入dataCount后返回，如果是整点则直接返回dataCount
        if (currentTime % (60 * 60) != 0) {

            JSONObject tmpPeriodData = new JSONObject();
            tmpPeriodData.put("recordTime", currentTime);
            tmpPeriodData.put("queryInPeriod", IndustryQueryUtil.tmpArray.clone());


            List<JSONObject> tmpDataCount = new LinkedList<>();
            for (JSONObject jsonObject : dataCount) tmpDataCount.add((JSONObject) jsonObject.clone());
            if (tmpDataCount.size() == 4)
                tmpDataCount.remove(0);
            tmpDataCount.add(tmpPeriodData);
            return tmpDataCount;
        }

        return dataCount;
    }
}
