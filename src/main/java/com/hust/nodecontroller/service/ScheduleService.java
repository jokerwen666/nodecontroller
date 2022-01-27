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
    @Scheduled(cron = "0 0/10 * * * ? ")
    public void calMultipleIdentityList() {
        long currentTime = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("OID", CalStateUtil.differOid());
        jsonObject.put("Ecode", CalStateUtil.differEcode());
        jsonObject.put("Handle", CalStateUtil.differHandle());
        jsonObject.put("DNS", CalStateUtil.differDns());
        jsonObject.put("DEIS", CalStateUtil.differDht());
        jsonObject.put("time", currentTime);
        CalStateUtil.preOidQueryCount = CalStateUtil.oidQueryCount;
        CalStateUtil.preEcodeQueryCount = CalStateUtil.ecodeQueryCount;
        CalStateUtil.preHandleQueryCount = CalStateUtil.handleQueryCount;
        CalStateUtil.preDnsQueryCount = CalStateUtil.dnsQueryCount;
        CalStateUtil.preDhtQueryCount = CalStateUtil.dhtQueryCount;

        if (CalStateUtil.multipleIdentityList.size() == 9) {
            CalStateUtil.multipleIdentityList.remove(0);
        }
        CalStateUtil.multipleIdentityList.add(jsonObject);
        logger.info("Calculate multiple identity query count per 10mins");
    }

    @Async("scheduleExecutor")
    @Scheduled(cron = "0/10 * * * * ? ")
    public void calMinuteSysInfo() {
        long currentTime = System.currentTimeMillis();
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
        long currentTime = System.currentTimeMillis();
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
        long currentTime = System.currentTimeMillis() / 1000;

        // 当前时刻为整点时，将一小时内的数据放进json数组中，如果一小时内的数据不够12个（比如不是在整点启动系统时），向前补0
        if (currentTime % (60 * 60) == 0) {
             while (IndustryQueryUtil.getTmpArray().size() < 12) {
                 IndustryQueryUtil.getTmpArray().add(0,0);
             }

             // 将一小时内的查询量放入periodData中
             JSONObject tmpPeriodData = new JSONObject();
             tmpPeriodData.put("recordTime", currentTime);
             tmpPeriodData.put("queryInPeriod", IndustryQueryUtil.getTmpArray().clone());
             IndustryQueryUtil.setPeriodData(tmpPeriodData);

            // json数组最多保存4个小时内的数据
            if (IndustryQueryUtil.getDataCount().size() == 4) {
                IndustryQueryUtil.getDataCount().remove(0);
            }
            // 将periodData放入查询结果dataCount中，并清空periodData和tmpArray
            IndustryQueryUtil.getDataCount().add((JSONObject) IndustryQueryUtil.getPeriodData().clone());
            IndustryQueryUtil.getPeriodData().clear();
            IndustryQueryUtil.getTmpArray().clear();

        }

        logger.info("calculate industry query count per 5min");
        IndustryQueryUtil.getTmpArray().add(IndustryQueryUtil.getQueryCount() - IndustryQueryUtil.getPreQueryCount());
        IndustryQueryUtil.setPreQueryCount(IndustryQueryUtil.getQueryCount());
    }
}
