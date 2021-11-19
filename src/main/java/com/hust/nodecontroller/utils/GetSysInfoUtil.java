package com.hust.nodecontroller.utils;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.communication.ComInfoModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import org.apache.commons.io.FileSystemUtils;
import sun.awt.image.ImageWatched;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName GetSysInfoUtil
 * @date 2020.10.18 16:18
 */


@EnableScheduling
public class GetSysInfoUtil {
    public static List<JSONObject> sysInfoList = new LinkedList<>();





    public static double CpuInfo() {
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();

        // 睡眠1s，否则出错
        try {
            TimeUnit.SECONDS.sleep(1);
        }catch(Exception e) {
            e.printStackTrace();
        }
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + ioWait + irq + softIrq + steal;
        return 1.0-(idle * 1.0 / totalCpu);
    }

    public static double MemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        //总内存
        long totalByte = memory.getTotal();
        //剩余内存
        long availableByte = memory.getAvailable();
        return (totalByte-availableByte)*1.0/totalByte;
    }

    public static double MemTotal() {
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        return memory.getTotal()/1024/1024/1024;
    }

    public static double DiskTotal() {
        return new File("/").getTotalSpace()/1024/1024/1024;
    }

    public static double FlowTotal() throws IOException {
        int flow;
        try {
            Process p = Runtime.getRuntime().exec("cat /proc/net/dev| grep ens33 |awk '{print $2,$10}'");
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String readLine = br.readLine();
            String[] strArr = readLine.split("\\ ");
            flow=(Integer.parseInt(strArr[0])+Integer.parseInt(strArr[1]))/1000;  //所有总flow单位KB，非单位时间

        }catch (Exception e) {
            e.printStackTrace();
            throw e;

        }

        return (double)flow ;
    }

    public static List<JSONObject> getMinuteSysInfo() {
        return sysInfoList;
    }
}
