package com.hust.nodecontroller.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class IndustryQueryUtil {
    public static List<JSONObject> dataCount = new LinkedList<>();
    public static ArrayList<Integer> tmpArray = new ArrayList<>();
    public static JSONObject periodData = new JSONObject();
    public static int queryCount = 0;
    public static int preQueryCount = 0;

    public static List<JSONObject> calIndustryQueryInfo() throws InterruptedException {
        long currentTime = new Date().getTime() / 1000;

        // 如果当前请求时刻不是整点，则将tmparray中数据临时添加入dataCount后返回，如果是整点则直接返回dataCount
        if (currentTime % (60 * 60) != 0) {
            Thread.sleep(100);
            JSONObject tmpPeriodData = new JSONObject();
            tmpPeriodData.put("recordTime", currentTime);
            tmpPeriodData.put("queryInPeriod", getTmpArray().clone());
            List<JSONObject> tmpDataCount = new LinkedList<>();

            for (int i= 0; i < getDataCount().size(); i++) {
                JSONObject jsonObject = (JSONObject) getDataCount().get(i).clone();
                tmpDataCount.add(jsonObject);
            }

            if (tmpDataCount.size() == 4)
                tmpDataCount.remove(0);
            tmpDataCount.add(tmpPeriodData);
            return tmpDataCount;
        }

        return getDataCount();
    }

    // 使用synchronized修饰静态方法，相当于对类上锁，保证只能有一个线程执行该静态方法
    public static synchronized List<JSONObject> getDataCount() {
        return dataCount;
    }

    public static synchronized void setDataCount(List<JSONObject> dataCount) {
        IndustryQueryUtil.dataCount = dataCount;
    }

    public static synchronized ArrayList<Integer> getTmpArray() {
        return tmpArray;
    }

    public static synchronized void setTmpArray(ArrayList<Integer> tmpArray) {
        IndustryQueryUtil.tmpArray = tmpArray;
    }

    public static synchronized JSONObject getPeriodData() {
        return periodData;
    }

    public static synchronized void setPeriodData(JSONObject periodData) {
        IndustryQueryUtil.periodData = periodData;
    }

    public static synchronized int getQueryCount() {
        return queryCount;
    }

    public static synchronized void setQueryCount(int queryCount) {
        IndustryQueryUtil.queryCount = queryCount;
    }

    public static synchronized int getPreQueryCount() {
        return preQueryCount;
    }

    public static synchronized void setPreQueryCount(int preQueryCount) {
        IndustryQueryUtil.preQueryCount = preQueryCount;
    }
}
