package com.hust.nodecontroller.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;

public class IndustryQueryUtil {
    public static List<JSONObject> dataCount = new LinkedList<>();
    public static ArrayList<Integer> tmpArray = new ArrayList<>();
    public static JSONObject periodData = new JSONObject();
    public static int queryCount = 0;
    public static int preQueryCount = 0;

    public static List<JSONObject> calIndustryQueryInfo() throws InterruptedException {
        Thread.sleep(200);
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
