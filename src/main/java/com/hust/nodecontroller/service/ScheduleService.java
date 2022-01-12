package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.utils.CalStateUtil;
import com.hust.nodecontroller.utils.GetSysInfoUtil;
import com.hust.nodecontroller.utils.IndustryQueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Date;

@Service
public class ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    // @Scheduled定时任务，默认在一个单线程中运行，一个定时任务若发生错误则可能导致其他任务被阻塞无法执行
    // 这里给每个定时任务分配一个线程池，每次定时任务启动的时候，都会创建一个单独的线程来处理。也就是说同一个定时任务也会启动多个线程处理
    // 任务1和任务2一起处理如果任务1导致线程1卡死，也不会影响到线程2

    @Async("scheduleExecutor")
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
        logger.info("calculate system-info per 10s");
    }

    @Async("scheduleExecutor")
    @Scheduled(cron = "0/10 * * * * ? ")
    public void calMinuteRuntimeInfo() throws IOException {
        long currentTime = new Date().getTime();
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        jsonObject.put("queryCount", CalStateUtil.differQuery());
        jsonObject.put("registerCount", CalStateUtil.differRegister());
        jsonObject.put("time", currentTime);

        jsonObject1.put("successRate", CalStateUtil.getSuccessRate());
        jsonObject1.put("totalCount", CalStateUtil.differTotal());
        jsonObject1.put("queryTimeout", CalStateUtil.getQueryTimeout());
        jsonObject1.put("time", currentTime);

        CalStateUtil.preQueryCount = CalStateUtil.queryCount;
        CalStateUtil.preRegisterCount = CalStateUtil.registerCount;
        CalStateUtil.preSuccessCount = CalStateUtil.successCount;
        CalStateUtil.preTotalCount = CalStateUtil.totalCount;
        CalStateUtil.preQueryTimeout = CalStateUtil.queryTimeout;

        if (CalStateUtil.runtimeInfoList1.size() == 6) {
            CalStateUtil.runtimeInfoList1.remove(0);
        }
        CalStateUtil.runtimeInfoList1.add(jsonObject);

        if (CalStateUtil.runtimeInfoList2.size() == 6) {
            CalStateUtil.runtimeInfoList2.remove(0);
        }
        CalStateUtil.runtimeInfoList2.add(jsonObject1);
        logger.info("calculate runtime-info per 10s");
    }

    @Async("scheduleExecutor")
    @Scheduled(cron = "0 0/5 * * * ?")
    public void calFiveMinuteQueryInfo() throws Exception {
        long currentTime = new Date().getTime() / 1000;

        // 当前时刻为整点时，将一小时内的数据放进json数组中，如果一小时内的数据不够12个（比如不是在整点启动系统时），向前补0
        if (currentTime % (60 * 60) == 0) {
             while (IndustryQueryUtil.tmpArray.size() < 12)
                IndustryQueryUtil.tmpArray.add(0,0);
            IndustryQueryUtil.periodData.put("recordTime", currentTime);
            IndustryQueryUtil.periodData.put("queryInPeriod", IndustryQueryUtil.tmpArray.clone());

            // json数组最多保存4个小时内的数据
            if (IndustryQueryUtil.dataCount.size() == 4) {
                IndustryQueryUtil.dataCount.remove(0);
            }

            IndustryQueryUtil.dataCount.add((JSONObject) IndustryQueryUtil.periodData.clone());
            IndustryQueryUtil.periodData.clear();
            IndustryQueryUtil.tmpArray.clear();

        }

        logger.info("calculate industry query count per 5min");
        IndustryQueryUtil.tmpArray.add(IndustryQueryUtil.queryCount - IndustryQueryUtil.preQueryCount);
        IndustryQueryUtil.preQueryCount = IndustryQueryUtil.queryCount;
    }
}
