package com.hust.nodecontroller.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName CalStateUtil
 * @date 2020.10.17 18:15
 */

@Component
public class CalStateUtil {
    private static final Logger logger = LoggerFactory.getLogger(CalStateUtil.class);
    public volatile static int preQueryCount = 0; // 10s前解析总量
    public volatile static int preRegisterCount = 0; // 10s前注册总量
    public volatile static int lastDayQueryCount = 0; // 前一天解析总量
    public volatile static int lastDayRegisterCount = 0; // 前一天注册总量
    public volatile static int preSuccessCount = 0; // 10s前成功解析总量
    public volatile static int preTotalCount = 0; // 10s前总操作量
    public volatile static int preTimeoutCount = 0; // 10s前总时延对应的解析量
    public volatile static long preQueryTimeout = 0; // 10s前总时延
    public volatile static int preOidQueryCount = 0; // 10min前oid总解析量
    public volatile static int preEcodeQueryCount = 0; // 10min前ecode总解析量
    public volatile static int preHandleQueryCount = 0; // 10min前handle总解析量
    public volatile static int preDnsQueryCount = 0; // 10min前dns总解析量
    public volatile static int preDhtQueryCount = 0; // 10min前dht总解析量

    public volatile static int queryCount = 0; // 解析总量
    public volatile static int registerCount = 0; // 注册总量
    public volatile static int successCount = 0; // 成功解析总量
    public volatile static int totalCount = 0; // 总操作量
    public volatile static int timeoutCount = 0; // 总时延对应的解析量
    public volatile static long queryTimeout = 0; // 总时延
    public volatile static int oidQueryCount = 0; // oid总解析量
    public volatile static int ecodeQueryCount = 0; // ecode总解析量
    public volatile static int handleQueryCount = 0; // handle总解析量
    public volatile static int dnsQueryCount = 0; // dns总解析量
    public volatile static int dhtQueryCount = 0; // dht总解析量

    public volatile static List<JSONObject> runtimeInfoList1 = new LinkedList<>();
    public volatile static List<JSONObject> runtimeInfoList2 = new LinkedList<>();
    public volatile static List<JSONObject> multipleIdentityList = new LinkedList<>();


    public CalStateUtil() {
//        Calendar now  = Calendar.getInstance();
//        now.set(Calendar.HOUR_OF_DAY, 0);
//        now.set(Calendar.MINUTE, 0);
//        now.set(Calendar.SECOND, 0);
//        now.set(Calendar.MILLISECOND, 0);
//        int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
//        for (int i = 5; i >= 0; i--) {
//            now.set(Calendar.DAY_OF_MONTH, dayOfMonth-i);
//            long currentTime = now.getTimeInMillis();
//            JSONObject runtimeInfo = new JSONObject();
//            runtimeInfo.put("registerCount", 0);
//            runtimeInfo.put("queryCount", 0);
//            runtimeInfo.put("time", currentTime);
//            runtimeInfoList1.add(runtimeInfo);
//        }
//
        preQueryCount = 1551;
        lastDayQueryCount = 1551;
        queryCount = 1551;

        preRegisterCount = 800;
        registerCount = 800;


        Calendar now  = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);

        for (int i = 5; i >= 3; i--) {
            now.set(Calendar.DAY_OF_MONTH, dayOfMonth-i);
            long currentTime = now.getTimeInMillis();
            JSONObject runtimeInfo = new JSONObject();
            runtimeInfo.put("registerCount", 0);
            runtimeInfo.put("queryCount", 0);
            runtimeInfo.put("time", currentTime);
            runtimeInfoList1.add(runtimeInfo);
        }

        now.set(Calendar.DAY_OF_MONTH, dayOfMonth-2);
        long currentTime1 = now.getTimeInMillis();
        JSONObject runtimeInfo1 = new JSONObject();
        runtimeInfo1.put("registerCount", 0);
        runtimeInfo1.put("queryCount", 1494);
        runtimeInfo1.put("time", currentTime1);
        runtimeInfoList1.add(runtimeInfo1);

        now.set(Calendar.DAY_OF_MONTH, dayOfMonth-1);
        long currentTime2 = now.getTimeInMillis();
        JSONObject runtimeInfo2 = new JSONObject();
        runtimeInfo2.put("registerCount", 205);
        runtimeInfo2.put("queryCount", 57);
        runtimeInfo2.put("time", currentTime2);
        runtimeInfoList1.add(runtimeInfo2);

        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        long currentTime3 = now.getTimeInMillis();
        JSONObject runtimeInfo3 = new JSONObject();
        runtimeInfo3.put("registerCount", 205);
        runtimeInfo3.put("queryCount", 57);
        runtimeInfo3.put("time", currentTime3);
        runtimeInfoList1.add(runtimeInfo3);
    }

    public static int differQuery(){ return queryCount-preQueryCount; }

    public static int differRegister(){
        return registerCount - preRegisterCount;
    }

    public static int differTotal(){
        return totalCount - preTotalCount;
    }

    public static int differOid() { return oidQueryCount - preOidQueryCount; }

    public static int differEcode() { return ecodeQueryCount - preEcodeQueryCount; }

    public static int differHandle() { return handleQueryCount - preHandleQueryCount; }

    public static int differDns() { return dnsQueryCount - preDnsQueryCount; }

    public static int differDht() { return dhtQueryCount - preDhtQueryCount; }

    public static List<JSONObject> getRuntimeInfoList2() {
        return runtimeInfoList2;
    }
    public static List<JSONObject> getMultipleIdentityList() {
        return multipleIdentityList;
    }

    public static float getSuccessRate(){
        int below = differQuery();
        if (below == 0) {
            return 0;
        }
        int top = successCount - preSuccessCount;
        logger.info("SuccessCount: {}, PreSuccessCount: {}", successCount, preSuccessCount);

        return (float) top / below;
    }

    public static float getQueryTimeout() {
        int below = timeoutCount - preTimeoutCount;
        if (below == 0) {
            return 0;
        }
        long top = queryTimeout - preQueryTimeout;

        logger.info("QueryTimeout: {}, PreQueryTimeout: {}", queryCount, preQueryTimeout);
        return (float) top / below;
    }

    public static List<JSONObject> getRuntimeInfoList1() throws InterruptedException {
        Thread.sleep(200);
        return runtimeInfoList1;
    }
}
