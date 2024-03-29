package com.hust.nodecontroller.communication;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.aop.ServiceAspect;
import com.hust.nodecontroller.infostruct.BulkInfo;
import com.hust.nodecontroller.infostruct.IMSystemInfo;
import com.hust.nodecontroller.infostruct.NormalMsg;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
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

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalMsg> register(String id, String prefix, String url, String toUrl, int type) {
        NormalMsg normalMsg = registerAndUpdate(id, prefix, url, toUrl, type);
        return CompletableFuture.completedFuture(normalMsg);
    }

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalMsg> update(String id, String prefix, String url, String toUrl,int type) {
        NormalMsg normalMsg;
        normalMsg = registerAndUpdate(id, prefix, url, toUrl, type);
        return CompletableFuture.completedFuture(normalMsg);
    }

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalMsg> delete(String id, String prefix, String toUrl,int type) {
        long beginTime = System.nanoTime();
        JSONObject jsonToIMSystem = new JSONObject();
        jsonToIMSystem.put("orgname", prefix);
        jsonToIMSystem.put("Identity", id);
        jsonToIMSystem.put("type", type);
        jsonToIMSystem.put("ip port", "112.125.88.26 10106");

        NormalMsg normalMsg = new NormalMsg();

        try {
            normalMsg = PostRequestUtil.getNormalResponse_(toUrl,jsonToIMSystem);
            long endTime = System.nanoTime();
            logger.info("Delete Time({}ms)", (endTime-beginTime)/1000000);
            return CompletableFuture.completedFuture(normalMsg);
        }catch (Exception e){
            normalMsg.setStatus(0);
            normalMsg.setMessage(e.getMessage());
            return CompletableFuture.completedFuture(normalMsg);
        }
    }

    @Async("queryHandleExecutor")
    public CompletableFuture<IMSystemInfo> query(String identity, String prefix, String toUrl, Boolean crossDomain_flag) {
        JSONObject jsonToIMSystem = new JSONObject();
        jsonToIMSystem.put("orgname", prefix);
        jsonToIMSystem.put("Identity", identity);
        jsonToIMSystem.put("type", 1);
        jsonToIMSystem.put("crossDomain_flag", crossDomain_flag);
        IMSystemInfo imSystemInfo = new IMSystemInfo();
        try {
            imSystemInfo = PostRequestUtil.getIMQueryResponse(toUrl,jsonToIMSystem);
            return CompletableFuture.completedFuture(imSystemInfo);
        }catch (Exception e){
            imSystemInfo.setStatus(0);
            imSystemInfo.setMessage(e.getMessage());
            return CompletableFuture.completedFuture(imSystemInfo);
        }
    }

    private NormalMsg registerAndUpdate(String id, String prefix, String url, String toUrl, int type) {
        JSONObject jsonToIMSystem = new JSONObject();
        jsonToIMSystem.put("orgname", prefix);
        jsonToIMSystem.put("Identity", id);
        jsonToIMSystem.put("type",type);
        jsonToIMSystem.put("mappingdata", url);

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

    public BulkInfo bulkRegister(StringBuilder id, StringBuilder url, String toUrl){
        JSONObject jsonToIMSystem = new JSONObject();
        jsonToIMSystem.put("Identitys", id);
        jsonToIMSystem.put("type", 8);
        jsonToIMSystem.put("mappingdatas", url);

        BulkInfo bulkInfo = new BulkInfo();

        try {
            bulkInfo = PostRequestUtil.getBulkRegisterInfo(toUrl,jsonToIMSystem);
            return bulkInfo;

        }catch (Exception e){
            bulkInfo.setStatus(0);
            bulkInfo.setMessage(e.getMessage());
            return bulkInfo;
        }

    }

    public BulkInfo bulkQuery(StringBuilder id, String toUrl){
        JSONObject jsonToIMSystem = new JSONObject();
        jsonToIMSystem.put("Identitys", id);
        jsonToIMSystem.put("type", 1);

        BulkInfo bulkInfo = new BulkInfo();

        try {
            bulkInfo = PostRequestUtil.getBulkQueryInfo(toUrl,jsonToIMSystem);
            return bulkInfo;

        }catch (Exception e){
            bulkInfo.setStatus(0);
            bulkInfo.setMessage(e.getMessage());
            return bulkInfo;
        }
    }
}
