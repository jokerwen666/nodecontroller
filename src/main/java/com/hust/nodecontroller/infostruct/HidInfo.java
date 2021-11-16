package com.hust.nodecontroller.infostruct;

public class HidInfo extends NormalMsg{
    private int oidCount;
    private int ecodeCount;
    private int handleCount;
    private int deisCount;

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

    public int getDeisCount() {
        return deisCount;
    }

    public void setDeisCount(int deisCount) {
        this.deisCount = deisCount;
    }
}
