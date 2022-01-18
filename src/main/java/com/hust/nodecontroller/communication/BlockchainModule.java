package com.hust.nodecontroller.communication;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.IdentityInfo;
import com.hust.nodecontroller.infostruct.NormalMsg;
import com.hust.nodecontroller.infostruct.RVSystemInfo;
import com.hust.nodecontroller.utils.HashUtil;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName BlockchainModule
 * @date 2020.10.18 12:28
 */

@Component
public class BlockchainModule implements SendInfoToModule{
    private static final Logger logger = LoggerFactory.getLogger(BlockchainModule.class);

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalMsg> register(String id, String hash, String url, String toUrl, String queryPermissions) {
        long beginTime = System.nanoTime();
        NormalMsg normalMsg;
        normalMsg = registerAndUpdate(id, hash, url, toUrl, queryPermissions);
        long endTime = System.nanoTime();
        logger.info("Register Time({}ms)", (endTime-beginTime)/1000000);
        return CompletableFuture.completedFuture(normalMsg);
    }

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalMsg> update(String id, String hash, String url, String toUrl, String queryPermissions) {
        long beginTime = System.nanoTime();
        NormalMsg normalMsg;
        normalMsg = registerAndUpdate(id, hash, url, toUrl, queryPermissions);
        long endTime = System.nanoTime();
        logger.info("Update Time({}ms)", (endTime-beginTime)/1000000);
        return CompletableFuture.completedFuture(normalMsg);
    }

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalMsg> delete(String id, String toUrl) {
        long beginTime = System.nanoTime();
        JSONObject jsonToRvSystem = new JSONObject();
        jsonToRvSystem.put("peer_name","peer0");
        jsonToRvSystem.put("Identifier",id);
        NormalMsg normalMsg = new NormalMsg();

        try {
            normalMsg = PostRequestUtil.getNormalResponse(toUrl,jsonToRvSystem);
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
    public CompletableFuture<RVSystemInfo> query(String identity, String toUrl) {
        long beginTime = System.nanoTime();
        JSONObject jsonToRvSystem = new JSONObject();
        jsonToRvSystem.put("peer_name", "peer0");
        jsonToRvSystem.put("Identifier", identity);
        RVSystemInfo rvSystemInfo = new RVSystemInfo();
        try {
            rvSystemInfo = PostRequestUtil.getRVQueryResponse(toUrl,jsonToRvSystem);
            long endTime = System.nanoTime();
            logger.info("Query Time({}ms)", (endTime-beginTime)/1000000);
            return CompletableFuture.completedFuture(rvSystemInfo);
        }catch (Exception e){
            logger.info(String.valueOf(10));
            rvSystemInfo.setStatus(0);
            rvSystemInfo.setMessage(e.getMessage());
            return CompletableFuture.completedFuture(rvSystemInfo);
        }
    }

    public IdentityInfo prefixQuery(String prefix, String bcUrl, String matchString) {
        long beginTime = System.nanoTime();
        IdentityInfo identityInfo = new IdentityInfo();

        try {
            identityInfo = PostRequestUtil.getAllByPrefix(bcUrl,prefix,matchString);
            long endTime = System.nanoTime();
            logger.info("Query Time({}ms)", (endTime-beginTime)/1000000);
            return identityInfo;
        }catch (Exception e){
            identityInfo.setStatus(0);
            identityInfo.setMessage(e.getMessage());
            return identityInfo;
        }
    }

    public NormalMsg queryOwnerByPrefix(String prefix, String bcUrl) {
        JSONObject jsonToRvSystem = new JSONObject();
        jsonToRvSystem.put("peer_name","peer0");
        jsonToRvSystem.put("erp_name","");
        jsonToRvSystem.put("identity_prefix", prefix);
        jsonToRvSystem.put("public_key","");
        jsonToRvSystem.put("authority","");
        jsonToRvSystem.put("onwer","");

        NormalMsg normalMsg = new NormalMsg();

        try {
            normalMsg = PostRequestUtil.getOwnerQueryResponse(bcUrl,jsonToRvSystem);
            return normalMsg;

        } catch (Exception e) {
            normalMsg.setStatus(0);
            normalMsg.setMessage(e.getMessage());
            return normalMsg;
        }

    }

    private NormalMsg registerAndUpdate(String id, String hash, String url, String toUrl, String queryPermissions) {
        JSONObject jsonToRvSystem = new JSONObject();
        jsonToRvSystem.put("peer_name","peer0");
        jsonToRvSystem.put("Identifier",id);
        jsonToRvSystem.put("hash",hash);
        jsonToRvSystem.put("abstract", HashUtil.SM3Hash(url));
        jsonToRvSystem.put("permisssion", queryPermissions);

        NormalMsg normalMsg = new NormalMsg();

        try {
            normalMsg = PostRequestUtil.getNormalResponse(toUrl,jsonToRvSystem);
            return normalMsg;

        }catch (Exception e){
            normalMsg.setStatus(0);
            normalMsg.setMessage(e.getMessage());
            return normalMsg;
        }
    }
}
