package com.hust.nodecontroller.infostruct;

import com.hust.nodecontroller.utils.CalStateUtil;

public class DailyRuntimeInfo {
    private int queryCount;
    private int registerCount;
    private long currentTime;

    public int getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(int queryCount) {
        this.queryCount = queryCount;
    }

    public int getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(int registerCount) {
        this.registerCount = registerCount;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public void clear() {
        currentTime = 0L;
        queryCount = 0;
        registerCount = 0;
    }
}
