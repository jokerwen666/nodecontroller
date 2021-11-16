package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.utils.CalStateUtil;
import com.hust.nodecontroller.utils.GetSysInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Date;

@EnableScheduling
@Service
public class CalStateService {

    private static final Logger logger = LoggerFactory.getLogger(CalStateService.class);

    @Scheduled(cron = "0/10 * * * * ? ")
    public void calMinuteSysInfo() {
        long currentTime = new Date().getTime();
        JSONObject jsonObject = new JSONObject();
        Double cpuRate = GetSysInfoUtil.CpuInfo();
        Double memRate = GetSysInfoUtil.MemInfo();
        jsonObject.put("cpuRate", cpuRate);
        jsonObject.put("memRate", memRate);
        jsonObject.put("time", currentTime);
        if (GetSysInfoUtil.sysInfoList.size() == 6) {
            GetSysInfoUtil.sysInfoList.remove(0);
        }
        GetSysInfoUtil.sysInfoList.add(jsonObject);
    }

    @Scheduled(cron = "0/10 * * * * ? ")
    public void calMinuteRuntimeInfo() throws IOException {
        long currentTime = new Date().getTime();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("queryCount", CalStateUtil.differQuery());
        jsonObject.put("registerCount", CalStateUtil.differRegister());
        jsonObject.put("successRate", CalStateUtil.getSuccessRate());
        jsonObject.put("totalCount", CalStateUtil.differTotal());
        //jsonObject.put("flowCount", GetSysInfoUtil.FlowTotal());
        jsonObject.put("time", currentTime);

        if (CalStateUtil.runtimeInfoList.size() == 6) {
            CalStateUtil.runtimeInfoList.remove(0);
        }
        CalStateUtil.runtimeInfoList.add(jsonObject);
    }
}
