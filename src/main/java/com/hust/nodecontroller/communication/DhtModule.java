package com.hust.nodecontroller.communication;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.BulkInfo;
import com.hust.nodecontroller.infostruct.IMSystemInfo;
import com.hust.nodecontroller.infostruct.AnswerStruct.NormalMsg;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName DhtModule
 * @date 2020.10.18 11:56
 */

@Component
public class DhtModule implements SendInfoToModule{
    private static final Logger logger = LoggerFactory.getLogger(DhtModule.class);

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalMsg> register(String id, String prefix, String url, String toUrl, int type) {
        long beginTime = System.nanoTime();
        NormalMsg normalMsg = registerAndUpdate(id, prefix, url, toUrl, type);
        long endTime = System.nanoTime();
        logger.info("Register Time({}ms)", (endTime-beginTime)/1000000);
        return CompletableFuture.completedFuture(normalMsg);
    }

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalMsg> update(String id, String prefix, String url, String toUrl,int type) {
        long beginTime = System.nanoTime();
        NormalMsg normalMsg;
        normalMsg = registerAndUpdate(id, prefix, url, toUrl, type);
        long endTime = System.nanoTime();
        logger.info("Update Time({}ms)", (endTime-beginTime)/1000000);
        return CompletableFuture.completedFuture(normalMsg);
    }

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalMsg> delete(String id, String prefix, String toUrl,int type) {
        long beginTime = System.nanoTime();
        JSONObject jsonToImSystem = new JSONObject();
        jsonToImSystem.put("orgname", prefix);
        jsonToImSystem.put("Identity", id);
        jsonToImSystem.put("type", type);
        jsonToImSystem.put("ip port", "112.125.88.26 10106");

        NormalMsg normalMsg = new NormalMsg();

        try {
            normalMsg = PostRequestUtil.getNormalResponse_(toUrl,jsonToImSystem);
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
    public CompletableFuture<IMSystemInfo> query(String identity, String prefix, String toUrl, Boolean isCrossDomain) {
        long beginTime = System.nanoTime();
        JSONObject jsonToImSystem = new JSONObject();
        jsonToImSystem.put("orgname", prefix);
        jsonToImSystem.put("Identity", identity);
        jsonToImSystem.put("type", 1);
        jsonToImSystem.put("crossDomain_flag", isCrossDomain);
        IMSystemInfo imSystemInfo = new IMSystemInfo();
        try {
            imSystemInfo = PostRequestUtil.getIMQueryResponse(toUrl,jsonToImSystem);
            long endTime = System.nanoTime();
            logger.info("Query Time({}ms)", (endTime-beginTime)/1000000);
            return CompletableFuture.completedFuture(imSystemInfo);
        }catch (Exception e){
            imSystemInfo.setStatus(0);
            imSystemInfo.setMessage(e.getMessage());
            return CompletableFuture.completedFuture(imSystemInfo);
        }
    }

    private NormalMsg registerAndUpdate(String id, String prefix, String url, String toUrl, int type) {
        JSONObject jsonToImSystem = new JSONObject();
        jsonToImSystem.put("orgname", prefix);
        jsonToImSystem.put("Identity", id);
        jsonToImSystem.put("type",type);
        jsonToImSystem.put("mappingdata", url);

        NormalMsg normalMsg = new NormalMsg();

        try {
            normalMsg = PostRequestUtil.getNormalResponse_(toUrl,jsonToImSystem);
            return normalMsg;

        }catch (Exception e){
            normalMsg.setStatus(0);
            normalMsg.setMessage(e.getMessage());
            return normalMsg;
        }
    }

    @Deprecated
    public BulkInfo bulkRegister(StringBuilder id, StringBuilder url, String toUrl){
        JSONObject jsonToImSystem = new JSONObject();
        jsonToImSystem.put("Identitys", id);
        jsonToImSystem.put("type", 8);
        jsonToImSystem.put("mappingdatas", url);

        BulkInfo bulkInfo = new BulkInfo();

        try {
            bulkInfo = PostRequestUtil.getBulkRegisterInfo(toUrl,jsonToImSystem);
            return bulkInfo;

        }catch (Exception e){
            bulkInfo.setStatus(0);
            bulkInfo.setMessage(e.getMessage());
            return bulkInfo;
        }

    }

    public BulkInfo bulkQuery(StringBuilder id, String toUrl){
        JSONObject jsonToImSystem = new JSONObject();
        jsonToImSystem.put("Identitys", id);
        jsonToImSystem.put("type", 1);

        BulkInfo bulkInfo = new BulkInfo();

        try {
            bulkInfo = PostRequestUtil.getBulkQueryInfo(toUrl,jsonToImSystem);
            return bulkInfo;

        }catch (Exception e){
            bulkInfo.setStatus(0);
            bulkInfo.setMessage(e.getMessage());
            return bulkInfo;
        }
    }
}
