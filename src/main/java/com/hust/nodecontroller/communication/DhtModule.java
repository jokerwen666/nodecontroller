package com.hust.nodecontroller.communication;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.aop.ServiceAspect;
import com.hust.nodecontroller.infostruct.IMSystemInfo;
import com.hust.nodecontroller.infostruct.NormalMsg;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName DhtModule
 * @date 2020.10.18 11:56
 */

@Component
public class DhtModule implements sendInfoToModule{
    private static final Logger logger = LoggerFactory.getLogger(DhtModule.class);

    @Async
    public Future<NormalMsg> register(String id, String prefix, String url, String toUrl, int type) {
        long beginTime = System.nanoTime();
        NormalMsg normalMsg = new NormalMsg();
        normalMsg = registerAndUpdate(id, prefix, url, toUrl, type);
        long endTime = System.nanoTime();
        logger.info("Register Time({}ms)", (endTime-beginTime)/1000000);
        return new AsyncResult<>(normalMsg);
    }

    @Async
    public Future<NormalMsg> update(String id, String prefix, String url, String toUrl,int type) {
        long beginTime = System.nanoTime();
        NormalMsg normalMsg;
        normalMsg = registerAndUpdate(id, prefix, url, toUrl, type);
        long endTime = System.nanoTime();
        logger.info("Update Time({}ms)", (endTime-beginTime)/1000000);
        return new AsyncResult<>(normalMsg);
    }

    @Async
    public Future<NormalMsg> delete(String id, String prefix, String toUrl,int type) {
        long beginTime = System.nanoTime();
        JSONObject jsonToIMSystem = new JSONObject();
        jsonToIMSystem.put("orgname", prefix);
        jsonToIMSystem.put("Identity", id);
        jsonToIMSystem.put("type", type);
        jsonToIMSystem.put("ip port", "112.125.88.26 10666");

        NormalMsg normalMsg = new NormalMsg();

        try {
            normalMsg = PostRequestUtil.getNormalResponse_(toUrl,jsonToIMSystem);
            long endTime = System.nanoTime();
            logger.info("Delete Time({}ms)", (endTime-beginTime)/1000000);
            return new AsyncResult<>(normalMsg);
        }catch (Exception e){
            normalMsg.setStatus(0);
            normalMsg.setMessage(e.getMessage());
            return new AsyncResult<>(normalMsg);
        }
    }

    @Async
    public Future<IMSystemInfo> query(String identity, String prefix, String toUrl, Boolean crossDomain_flag) {
        long beginTime = System.nanoTime();
        JSONObject jsonToIMSystem = new JSONObject();
        jsonToIMSystem.put("orgname", prefix);
        jsonToIMSystem.put("Identity", identity);
        jsonToIMSystem.put("type", 1);
        jsonToIMSystem.put("ip port", "112.125.88.26 10666");
        jsonToIMSystem.put("crossDomain_flag", crossDomain_flag);
        IMSystemInfo imSystemInfo = new IMSystemInfo();
        try {
            imSystemInfo = PostRequestUtil.getIMQueryResponse(toUrl,jsonToIMSystem);
            long endTime = System.nanoTime();
            logger.info("Query Time({}ms)", (endTime-beginTime)/1000000);
            return new AsyncResult<>(imSystemInfo);
        }catch (Exception e){
            imSystemInfo.setStatus(0);
            imSystemInfo.setMessage(e.getMessage());
            return new AsyncResult<>(imSystemInfo);
        }
    }

    private NormalMsg registerAndUpdate(String id, String prefix, String url, String toUrl, int type) {
        JSONObject jsonToIMSystem = new JSONObject();
        jsonToIMSystem.put("orgname", prefix);
        jsonToIMSystem.put("Identity", id);
        jsonToIMSystem.put("type",type);
        jsonToIMSystem.put("mappingdata", url);
        jsonToIMSystem.put("ip port", "112.125.88.26 10666");

        NormalMsg normalMsg = new NormalMsg();

        try {
            normalMsg = PostRequestUtil.getNormalResponse_(toUrl,jsonToIMSystem);
            return normalMsg;

        }catch (Exception e){
            normalMsg.setStatus(0);
            normalMsg.setMessage(e.getMessage());
            return normalMsg;
        }
    }
}
