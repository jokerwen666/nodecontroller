package com.hust.nodecontroller.infostruct;

import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName RuntimeState
 * @date 2020.10.17 18:21
 */
@Component
public class RuntimeState extends NormalMsg{
    private int queryCount;
    private int registerCount;
    private float successRate;
    private int totalCount;
    private double flowCount;

    public double getFlowCount() {
        return flowCount;
    }

    public void setFlowCount(double flowCount) {
        this.flowCount = flowCount;
    }

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

    public float getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(float successRate) {
        this.successRate = successRate;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}