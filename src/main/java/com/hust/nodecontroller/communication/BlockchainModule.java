package com.hust.nodecontroller.communication;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.IdentityInfo;
import com.hust.nodecontroller.infostruct.NormalMsg;
import com.hust.nodecontroller.infostruct.RVSystemInfo;
import com.hust.nodecontroller.utils.EncDecUtil;
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
public class BlockchainModule implements sendInfoToModule{
    private static final Logger logger = LoggerFactory.getLogger(BlockchainModule.class);

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalMsg> register(String id, String hash, String url, String toUrl, String queryPermissions) {
        NormalMsg normalMsg;
        normalMsg = registerAndUpdate(id, hash, url, toUrl, queryPermissions);
        return CompletableFuture.completedFuture(normalMsg);
    }

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalMsg> update(String id, String hash, String url, String toUrl, String queryPermissions) {
        NormalMsg normalMsg;
        normalMsg = registerAndUpdate(id, hash, url, toUrl, queryPermissions);
        return CompletableFuture.completedFuture(normalMsg);
    }

    @Async("enterpriseHandleExecutor")
    public CompletableFuture<NormalMsg> delete(String id, String toUrl) {
        JSONObject jsonToRVSystem = new JSONObject();
        jsonToRVSystem.put("peer_name","peer0");
        jsonToRVSystem.put("Identifier",id);
        NormalMsg normalMsg = new NormalMsg();

        try {
            normalMsg = PostRequestUtil.getNormalResponse(toUrl,jsonToRVSystem);
            return CompletableFuture.completedFuture(normalMsg);
        }catch (Exception e){
            normalMsg.setStatus(0);
            normalMsg.setMessage(e.getMessage());
            return CompletableFuture.completedFuture(normalMsg);
        }
    }

    @Async("queryHandleExecutor")
    public CompletableFuture<RVSystemInfo> query(String identity, String toUrl) {
        JSONObject jsonToRVSystem = new JSONObject();
        jsonToRVSystem.put("peer_name", "peer0");
        jsonToRVSystem.put("Identifier", identity);
        RVSystemInfo rvSystemInfo = new RVSystemInfo();
        try {
            rvSystemInfo = PostRequestUtil.getRVQueryResponse(toUrl,jsonToRVSystem);
            return CompletableFuture.completedFuture(rvSystemInfo);
        }catch (Exception e){
            rvSystemInfo.setStatus(0);
            rvSystemInfo.setMessage(e.getMessage());
            return CompletableFuture.completedFuture(rvSystemInfo);
        }
    }

    public IdentityInfo prefixQuery(String prefix, String bcUrl, String matchString) {
        IdentityInfo identityInfo = new IdentityInfo();

        try {
            identityInfo = PostRequestUtil.getAllByPrefix(bcUrl,prefix,matchString);
            return identityInfo;
        }catch (Exception e){
            identityInfo.setStatus(0);
            identityInfo.setMessage(e.getMessage());
            return identityInfo;
        }
    }

    @Async("queryHandleExecutor")
    public CompletableFuture<NormalMsg> queryOwnerByPrefix(String prefix, String bcUrl) {
        JSONObject jsonToRVSystem = new JSONObject();
        jsonToRVSystem.put("peer_name","peer0");
        jsonToRVSystem.put("erp_name","");
        jsonToRVSystem.put("identity_prefix", prefix);
        jsonToRVSystem.put("public_key","");
        jsonToRVSystem.put("authority","");
        jsonToRVSystem.put("onwer","");

        NormalMsg normalMsg = new NormalMsg();

        try {
            normalMsg = PostRequestUtil.getOwnerQueryResponse(bcUrl,jsonToRVSystem);
            return CompletableFuture.completedFuture(normalMsg);

        } catch (Exception e) {
            normalMsg.setStatus(0);
            normalMsg.setMessage(e.getMessage());
            return CompletableFuture.completedFuture(normalMsg);
        }

    }

    private NormalMsg registerAndUpdate(String id, String hash, String url, String toUrl, String queryPermissions) {
        JSONObject jsonToRVSystem = new JSONObject();
        jsonToRVSystem.put("peer_name","peer0");
        jsonToRVSystem.put("Identifier",id);
        jsonToRVSystem.put("hash",hash);
        jsonToRVSystem.put("abstract", EncDecUtil.sMHash(url));
        jsonToRVSystem.put("permisssion", queryPermissions);

        NormalMsg normalMsg = new NormalMsg();

        try {
            normalMsg = PostRequestUtil.getNormalResponse(toUrl,jsonToRVSystem);
            return normalMsg;

        }catch (Exception e){
            normalMsg.setStatus(0);
            normalMsg.setMessage(e.getMessage());
            return normalMsg;
        }
    }
}
