package com.hust.nodecontroller.utils;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName CalStateUtil
 * @date 2020.10.17 18:15
 */

public class CalStateUtil {
    public static int preQueryCount = 0;
    public static int preRegisterCount = 0;
    public static int preSuccessCount = 0;
    public static int preTotalCount = 0;

    public static int queryCount = 0;
    public static int registerCount = 0;
    public static int successCount = 0;
    public static int totalCount = 0;

    public static int totalQuery = 0;
    public static long totalQueryTimeout = 0;

    public static int differQuery(){ return queryCount-preQueryCount; }

    public static int differRegister(){
        return registerCount - preRegisterCount;
    }

    public static int differTotal(){
        return totalCount - preTotalCount;
    }

    public static float getSuccessRate(){
        int below = differQuery();
        if (below == 0)
            return 0;
        int top = successCount - preSuccessCount;
        return (float)top/below;
    }

    public static void updateState(){
        preQueryCount = queryCount;
        preRegisterCount = registerCount;
        preSuccessCount = successCount;
        preTotalCount = totalCount;
    }
}
