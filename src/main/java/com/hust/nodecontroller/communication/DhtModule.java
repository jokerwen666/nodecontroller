package com.hust.nodecontroller.communication;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.exception.ControlSubSystemException;
import com.hust.nodecontroller.infostruct.answerstruct.IdentityManagementSystemAnswer;
import com.hust.nodecontroller.infostruct.answerstruct.NormalAnswer;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

/**
 * @author Zhang Bowen
 * @Description 标识管理子系统（DHT）交互类
 * @ClassName BlockchainModule
 * @date 2020.10.18 12:28
 */

@Component
public class DhtModule implements SendInfoToModule{
    private static final Logger logger = LoggerFactory.getLogger(DhtModule.class);

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalAnswer> register(String id, String prefix, String url, String toUrl, int type) {
        long beginTime = System.nanoTime();
        NormalAnswer normalAnswer = registerAndUpdate(id, prefix, url, toUrl, type);
        long endTime = System.nanoTime();
        logger.info("Register Time({}ms)", (endTime-beginTime)/1000000);
        return CompletableFuture.completedFuture(normalAnswer);
    }

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalAnswer> update(String id, String prefix, String url, String toUrl, int type) {
        long beginTime = System.nanoTime();
        NormalAnswer normalAnswer;
        normalAnswer = registerAndUpdate(id, prefix, url, toUrl, type);
        long endTime = System.nanoTime();
        logger.info("Update Time({}ms)", (endTime-beginTime)/1000000);
        return CompletableFuture.completedFuture(normalAnswer);
    }

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalAnswer> delete(String id, String prefix, String toUrl, int type) {
        long beginTime = System.nanoTime();
        JSONObject jsonToImSystem = new JSONObject();
        jsonToImSystem.put("orgname", prefix);
        jsonToImSystem.put("Identity", id);
        jsonToImSystem.put("type", type);
        jsonToImSystem.put("ip port", "112.125.88.26 10106");

        NormalAnswer normalAnswer = new NormalAnswer();

        try {
            normalAnswer = PostRequestUtil.getDhtAnswer(toUrl,jsonToImSystem);
            long endTime = System.nanoTime();
            logger.info("Delete Time({}ms)", (endTime-beginTime)/1000000);
            return CompletableFuture.completedFuture(normalAnswer);
        }catch (ControlSubSystemException e){
            normalAnswer.setStatus(0);
            normalAnswer.setMessage(e.getMessage());
            return CompletableFuture.completedFuture(normalAnswer);
        }
    }

    @Async("queryHandleExecutor")
    public CompletableFuture<IdentityManagementSystemAnswer> query(String identity, String prefix, String toUrl, Boolean isCrossDomain) {
        long beginTime = System.nanoTime();
        JSONObject jsonToImSystem = new JSONObject();
        jsonToImSystem.put("orgname", prefix);
        jsonToImSystem.put("Identity", identity);
        jsonToImSystem.put("type", 1);
        jsonToImSystem.put("crossDomain_flag", isCrossDomain);
        IdentityManagementSystemAnswer identityManagementSystemAnswer = new IdentityManagementSystemAnswer();
        try {
            identityManagementSystemAnswer = PostRequestUtil.getIdentityManagementAnswer(toUrl,jsonToImSystem);
            long endTime = System.nanoTime();
            logger.info("Query Time({}ms)", (endTime-beginTime)/1000000);
            return CompletableFuture.completedFuture(identityManagementSystemAnswer);
        }catch (ControlSubSystemException e){
            identityManagementSystemAnswer.setStatus(0);
            identityManagementSystemAnswer.setMessage(e.getMessage());
            return CompletableFuture.completedFuture(identityManagementSystemAnswer);
        }
    }

    private NormalAnswer registerAndUpdate(String id, String prefix, String url, String toUrl, int type) {
        JSONObject jsonToImSystem = new JSONObject();
        jsonToImSystem.put("orgname", prefix);
        jsonToImSystem.put("Identity", id);
        jsonToImSystem.put("type",type);
        jsonToImSystem.put("mappingdata", url);

        NormalAnswer normalAnswer = new NormalAnswer();

        try {
            normalAnswer = PostRequestUtil.getDhtAnswer(toUrl,jsonToImSystem);
            return normalAnswer;

        }catch (ControlSubSystemException e){
            normalAnswer.setStatus(0);
            normalAnswer.setMessage(e.getMessage());
            return normalAnswer;
        }
    }
}
