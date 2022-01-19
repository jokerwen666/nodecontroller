package com.hust.nodecontroller.infostruct;

public class HidInfo extends NormalMsg{
    private int oidCount;
    private int ecodeCount;
    private int handleCount;
    private int dnsCount;
    private int dhtCount;

    public int getOidCount() {
        return oidCount;
    }

    public void setOidCount(int oidCount) {
        this.oidCount = oidCount;
    }

    public int getEcodeCount() {
        return ecodeCount;
    }

    public void setEcodeCount(int ecodeCount) {
        this.ecodeCount = ecodeCount;
    }

    public int getHandleCount() {
        return handleCount;
    }

    public void setHandleCount(int handleCount) {
        this.handleCount = handleCount;
    }

    public int getDnsCount() {
        return dnsCount;
    }

    public void setDnsCount(int dnsCount) {
        this.dnsCount = dnsCount;
    }

    public int getDhtCount() {
        return dhtCount;
    }

    public void setDhtCount(int dhtCount) {
        this.dhtCount = dhtCount;
    }
}
