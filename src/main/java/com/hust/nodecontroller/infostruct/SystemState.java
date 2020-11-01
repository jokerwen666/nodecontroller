package com.hust.nodecontroller.infostruct;

import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName SystemState
 * @date 2020.10.17 19:12
 */

@Component
public class SystemState extends NormalMsg{
    private double cpuRate;
    private double memRate;

    public double getCpuRate() {
        return cpuRate;
    }

    public void setCpuRate(double cpuRate) {
        this.cpuRate = cpuRate;
    }

    public double getMemRate() {
        return memRate;
    }

    public void setMemRate(double memRate) {
        this.memRate = memRate;
    }
}
