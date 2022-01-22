package com.hust.nodecontroller.communication;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.exception.ControlSubSystemException;
import com.hust.nodecontroller.infostruct.answerstruct.QueryAllByPrefixAnswer;
import com.hust.nodecontroller.infostruct.answerstruct.NormalAnswer;
import com.hust.nodecontroller.infostruct.answerstruct.ResultVerifySystemAnswer;
import com.hust.nodecontroller.utils.HashUtil;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

/**
 * @author Zhang Bowen
 * @Description 结果验证子系统（区块链）交互类
 * @ClassName BlockchainModule
 * @date 2020.10.18 12:28
 */

@Component
public class BlockchainModule implements SendInfoToModule{
    private static final Logger logger = LoggerFactory.getLogger(BlockchainModule.class);

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalAnswer> register(String id, String hash, String url, String toUrl, String queryPermissions) {
        long beginTime = System.nanoTime();
        NormalAnswer normalAnswer;
        normalAnswer = registerAndUpdate(id, hash, url, toUrl, queryPermissions);
        long endTime = System.nanoTime();
        logger.info("Register Time({}ms)", (endTime-beginTime)/1000000);
        return CompletableFuture.completedFuture(normalAnswer);
    }

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalAnswer> update(String id, String hash, String url, String toUrl, String queryPermissions) {
        long beginTime = System.nanoTime();
        NormalAnswer normalAnswer;
        normalAnswer = registerAndUpdate(id, hash, url, toUrl, queryPermissions);
        long endTime = System.nanoTime();
        logger.info("Update Time({}ms)", (endTime-beginTime)/1000000);
        return CompletableFuture.completedFuture(normalAnswer);
    }

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalAnswer> delete(String id, String toUrl) {
        long beginTime = System.nanoTime();
        JSONObject jsonToRvSystem = new JSONObject();
        jsonToRvSystem.put("peer_name","peer0");
        jsonToRvSystem.put("Identifier",id);
        NormalAnswer normalAnswer = new NormalAnswer();

        try {
            normalAnswer = PostRequestUtil.getBlockChainAnswer(toUrl,jsonToRvSystem);
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
    public CompletableFuture<ResultVerifySystemAnswer> query(String identity, String toUrl) {
        long beginTime = System.nanoTime();
        JSONObject jsonToRvSystem = new JSONObject();
        jsonToRvSystem.put("peer_name", "peer0");
        jsonToRvSystem.put("Identifier", identity);
        ResultVerifySystemAnswer resultVerifySystemAnswer = new ResultVerifySystemAnswer();
        try {
            resultVerifySystemAnswer = PostRequestUtil.getResultVerifyAnswer(toUrl,jsonToRvSystem);
            long endTime = System.nanoTime();
            logger.info("Query Time({}ms)", (endTime-beginTime)/1000000);
            return CompletableFuture.completedFuture(resultVerifySystemAnswer);
        }catch (ControlSubSystemException e){
            logger.info(String.valueOf(10));
            resultVerifySystemAnswer.setStatus(0);
            resultVerifySystemAnswer.setMessage(e.getMessage());
            return CompletableFuture.completedFuture(resultVerifySystemAnswer);
        }
    }

    public QueryAllByPrefixAnswer prefixQuery(String prefix, String bcUrl, String matchString) {
        long beginTime = System.nanoTime();
        QueryAllByPrefixAnswer queryAllByPrefixAnswer = new QueryAllByPrefixAnswer();

        try {
            queryAllByPrefixAnswer = PostRequestUtil.getAllByPrefix(bcUrl,prefix,matchString);
            long endTime = System.nanoTime();
            logger.info("Query Time({}ms)", (endTime-beginTime)/1000000);
            return queryAllByPrefixAnswer;
        }catch (ControlSubSystemException e){
            queryAllByPrefixAnswer.setStatus(0);
            queryAllByPrefixAnswer.setMessage(e.getMessage());
            return queryAllByPrefixAnswer;
        }
    }

    public NormalAnswer queryOwnerByPrefix(String prefix, String bcUrl) {
        JSONObject jsonToRvSystem = new JSONObject();
        jsonToRvSystem.put("peer_name","peer0");
        jsonToRvSystem.put("erp_name","");
        jsonToRvSystem.put("identity_prefix", prefix);
        jsonToRvSystem.put("public_key","");
        jsonToRvSystem.put("authority","");
        jsonToRvSystem.put("onwer","");

        NormalAnswer normalAnswer = new NormalAnswer();

        try {
            normalAnswer = PostRequestUtil.queryIdentityOwnerAnswer(bcUrl,jsonToRvSystem);
            return normalAnswer;

        } catch (ControlSubSystemException e) {
            normalAnswer.setStatus(0);
            normalAnswer.setMessage(e.getMessage());
            return normalAnswer;
        }

    }

    private NormalAnswer registerAndUpdate(String id, String hash, String url, String toUrl, String queryPermissions) {
        JSONObject jsonToRvSystem = new JSONObject();
        jsonToRvSystem.put("peer_name","peer0");
        jsonToRvSystem.put("Identifier",id);
        jsonToRvSystem.put("hash",hash);
        jsonToRvSystem.put("abstract", HashUtil.SM3Hash(url));
        jsonToRvSystem.put("permisssion", queryPermissions);

        NormalAnswer normalAnswer = new NormalAnswer();

        try {
            normalAnswer = PostRequestUtil.getBlockChainAnswer(toUrl,jsonToRvSystem);
            return normalAnswer;

        }catch (Exception e){
            normalAnswer.setStatus(0);
            normalAnswer.setMessage(e.getMessage());
            return normalAnswer;
        }
    }
}
