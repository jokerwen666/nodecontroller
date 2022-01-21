package com.hust.nodecontroller.infostruct;

import com.hust.nodecontroller.infostruct.AnswerStruct.NormalAnswer;

public class HidInfo extends NormalAnswer {
    private int oidCount;
    private int ecodeCount;
    private int handleCount;
    private int dnsCount;

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
}
