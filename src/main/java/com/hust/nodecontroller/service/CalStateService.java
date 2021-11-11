package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.utils.GetSysInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@Service
public class CalStateService {

    private static final Logger logger = LoggerFactory.getLogger(CalStateService.class);

    @Scheduled(cron = "0/10 * * * * ? ")
    public void calMinuteSysInfo() throws InterruptedException {
        long currentTime = new Date().getTime();
        JSONObject jsonObject = new JSONObject();
        Double cpuRate = GetSysInfoUtil.CpuInfo();
        Double memRate = GetSysInfoUtil.MemInfo();
        jsonObject.put("cpuRate", cpuRate);
        jsonObject.put("memRate", memRate);
        jsonObject.put("times", currentTime);
        logger.info("add run-time info");
        if (GetSysInfoUtil.sysInfoList.size() < 6)
            GetSysInfoUtil.sysInfoList.add(jsonObject);
        else {
            GetSysInfoUtil.sysInfoList.remove(0);
            GetSysInfoUtil.sysInfoList.add(jsonObject);
        }
    }
}
