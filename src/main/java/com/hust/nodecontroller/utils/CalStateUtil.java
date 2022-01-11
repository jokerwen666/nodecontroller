package com.hust.nodecontroller.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName CalStateUtil
 * @date 2020.10.17 18:15
 */

public class CalStateUtil {
    private static final Logger logger = LoggerFactory.getLogger(CalStateUtil.class);

    public static int preQueryCount = 0;
    public static int preRegisterCount = 0;
    public static int preSuccessCount = 0;
    public static int preTotalCount = 0;
    public static long preQueryTimeout = 0;
    public static int preOidQueryCount = 0;
    public static int preEcodeQueryCount = 0;
    public static int preHandleQueryCount = 0;
    public static int preDnsQueryCount = 0;

    public static int queryCount = 0;
    public static int registerCount = 0;
    public static int successCount = 0;
    public static int totalCount = 0;
    public static long queryTimeout = 0;
    public static int oidQueryCount = 0;
    public static int ecodeQueryCount = 0;
    public static int handleQueryCount = 0;
    public static int dnsQueryCount = 0;

    public static List<JSONObject> runtimeInfoList1 = new LinkedList<>();
    public static List<JSONObject> runtimeInfoList2 = new LinkedList<>();

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

    public static List<JSONObject> getRuntimeInfoList1() {
        return runtimeInfoList1;
    }
    public static List<JSONObject> getRuntimeInfoList2() {
        return runtimeInfoList2;
    }

    public static float getSuccessRate(){
        int below = differQuery();
        if (below == 0)
            return 0;
        int top = successCount - preSuccessCount;

        logger.info("SuccessCount: {}, PreSuccessCount: {}", successCount, preSuccessCount);

        return (float) top / below;
    }

    public static float getQueryTimeout() {
        int below = differQuery();
        if (below == 0)
            return 0;
        long top = queryTimeout - preQueryTimeout;

        logger.info("QueryTimeout: {}, PreQueryTimeout: {}", queryCount, preQueryTimeout);
        return (float) top / below;
    }

}
