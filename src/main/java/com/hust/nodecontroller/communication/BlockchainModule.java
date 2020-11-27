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
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName BlockchainModule
 * @date 2020.10.18 12:28
 */

@Component
public class BlockchainModule implements sendInfoToModule{
    private static final Logger logger = LoggerFactory.getLogger(BlockchainModule.class);

    @Async
    public Future<NormalMsg> register(String id, String hash, String url, String toUrl) {
        long beginTime = System.nanoTime();
        NormalMsg normalMsg;
        normalMsg = registerAndUpdate(id, hash, url, toUrl);
        long endTime = System.nanoTime();
        logger.info("Register Time({}ms)", (endTime-beginTime)/1000000);
        return new AsyncResult<>(normalMsg);
    }

    @Async
    public Future<NormalMsg> update(String id, String hash, String url, String toUrl) {
        long beginTime = System.nanoTime();
        NormalMsg normalMsg;
        normalMsg = registerAndUpdate(id, hash, url, toUrl);
        long endTime = System.nanoTime();
        logger.info("Update Time({}ms)", (endTime-beginTime)/1000000);
        return new AsyncResult<>(normalMsg);
    }

    @Async
    public Future<NormalMsg> delete(String id, String toUrl) {
        long beginTime = System.nanoTime();
        JSONObject jsonToRVSystem = new JSONObject();
        jsonToRVSystem.put("peer_name","peer0");
        jsonToRVSystem.put("Identifier",id);
        NormalMsg normalMsg = new NormalMsg();

        try {
            normalMsg = PostRequestUtil.getNormalResponse(toUrl,jsonToRVSystem);
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
    public Future<RVSystemInfo> query(String identity, String toUrl) {
        long beginTime = System.nanoTime();
        JSONObject jsonToRVSystem = new JSONObject();
        jsonToRVSystem.put("peer_name", "peer0");
        jsonToRVSystem.put("Identifier", identity);
        RVSystemInfo rvSystemInfo = new RVSystemInfo();
        try {
            rvSystemInfo = PostRequestUtil.getRVQueryResponse(toUrl,jsonToRVSystem);
            long endTime = System.nanoTime();
            logger.info("Query Time({}ms)", (endTime-beginTime)/1000000);
            return new AsyncResult<>(rvSystemInfo);
        }catch (Exception e){
            rvSystemInfo.setStatus(0);
            rvSystemInfo.setMessage(e.getMessage());
            return new AsyncResult<>(rvSystemInfo);
        }
    }

    public IdentityInfo prefixQuery(String prefix, String bcUrl) {
        long beginTime = System.nanoTime();
        IdentityInfo identityInfo = new IdentityInfo();

        try {
            identityInfo = PostRequestUtil.getAllByPrefix(bcUrl,prefix);
            long endTime = System.nanoTime();
            logger.info("Query Time({}ms)", (endTime-beginTime)/1000000);
            return identityInfo;
        }catch (Exception e){
            identityInfo.setStatus(0);
            identityInfo.setMessage(e.getMessage());
            return identityInfo;
        }
    }

    private NormalMsg registerAndUpdate(String id, String hash, String url, String toUrl) {
        JSONObject jsonToRVSystem = new JSONObject();
        jsonToRVSystem.put("peer_name","peer0");
        jsonToRVSystem.put("Identifier",id);
        jsonToRVSystem.put("hash",hash);
        jsonToRVSystem.put("abstract", Integer.toHexString(HashUtil.apHash(url)));

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
