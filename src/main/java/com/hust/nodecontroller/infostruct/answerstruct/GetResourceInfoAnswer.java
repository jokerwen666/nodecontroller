package com.hust.nodecontroller.infostruct.answerstruct;


import com.hust.nodecontroller.infostruct.answerstruct.NormalAnswer;

public class GetResourceInfoAnswer extends NormalAnswer {
    private int idCount;
    private int queryCount;
    private float queryTimeout;
    private double memTotal;
    private double diskTotal;

    public int getIdCount() {
        return idCount;
    }

    public void setIdCount(int idCount) {
        this.idCount = idCount;
    }

    public int getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(int queryCount) {
        this.queryCount = queryCount;
    }

    public double getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(double memTotal) {
        this.memTotal = memTotal;
    }

    public double getDiskTotal() {
        return diskTotal;
    }

    public void setDiskTotal(double diskTotal) {
        this.diskTotal = diskTotal;
    }

    public float getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(float queryTimeout) {
        this.queryTimeout = queryTimeout;
    }
}
