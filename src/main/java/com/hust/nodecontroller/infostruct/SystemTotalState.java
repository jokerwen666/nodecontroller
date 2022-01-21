package com.hust.nodecontroller.infostruct;

import com.hust.nodecontroller.infostruct.AnswerStruct.NormalAnswer;

public class SystemTotalState extends NormalAnswer {
    private int totalNodeCount;
    private int totalIndustryCount;
    private long systemQueryTimeout;

    public int getTotalNodeCount() {
        return totalNodeCount;
    }

    public void setTotalNodeCount(int totalNodeCount) {
        this.totalNodeCount = totalNodeCount;
    }

    public int getTotalIndustryCount() {
        return totalIndustryCount;
    }

    public void setTotalIndustryCount(int totalIndustryCount) {
        this.totalIndustryCount = totalIndustryCount;
    }

    public long getSystemQueryTimeout() {
        return systemQueryTimeout;
    }

    public void setSystemQueryTimeout(long systemQueryTimeout) {
        this.systemQueryTimeout = systemQueryTimeout;
    }
}
