package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.communication.AuthorityModule;
import com.hust.nodecontroller.communication.BlockchainModule;
import com.hust.nodecontroller.communication.ComInfoModule;
import com.hust.nodecontroller.communication.DhtModule;
import com.hust.nodecontroller.enums.ErrorMessageEnum;
import com.hust.nodecontroller.enums.RequestTypeEnum;
import com.hust.nodecontroller.errorhandle.BCErrorHandle;
import com.hust.nodecontroller.errorhandle.DhtErrorHandle;
import com.hust.nodecontroller.exception.ControlSubSystemException;
import com.hust.nodecontroller.infostruct.*;
import com.hust.nodecontroller.infostruct.AnswerStruct.AllPrefixIdAnswer;
import com.hust.nodecontroller.infostruct.AnswerStruct.IdentityInfo;
import com.hust.nodecontroller.infostruct.AnswerStruct.NormalMsg;
import com.hust.nodecontroller.utils.CalStateUtil;
import com.hust.nodecontroller.utils.EncDecUtil;
import com.hust.nodecontroller.utils.HashUtil;
import com.hust.nodecontroller.utils.IndustryQueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName ControlProcess
 * @date 2020.10.07 14:25
 */

@Component
public class ControlProcessImpl implements ControlProcess{

    private final int MODULE_TYPE_DHT = 0;
    private final int MODULE_TYPE_BC = 1;
    private final DhtModule dhtModule;
    private final BlockchainModule blockchainModule;
    private final AuthorityModule authorityModule;
    private final ComInfoModule comInfoModule;
    private final BCErrorHandle bcErrorHandle;
    private final DhtErrorHandle dhtErrorHandle;
    private static final Logger logger = LoggerFactory.getLogger(ControlProcessImpl.class);
    public static String ownDomainPrefix;

    @Value("${domain.prefix}")
    public void setDomainPrefix(String prefix) {
        ownDomainPrefix = prefix;
    }


    @Autowired
    public ControlProcessImpl(DhtModule dhtModule, BlockchainModule blockchainModule, AuthorityModule authorityModule, ComInfoModule comInfoModule, BCErrorHandle bcErrorHandle, DhtErrorHandle dhtErrorHandle) {
        this.dhtModule = dhtModule;
        this.blockchainModule = blockchainModule;
        this.authorityModule = authorityModule;
        this.comInfoModule = comInfoModule;
        this.bcErrorHandle = bcErrorHandle;
        this.dhtErrorHandle = dhtErrorHandle;
    }


    @Override
    public void registerAndUpdateHandle (String client, String identity, String prefix, JSONObject data, String dhtUrl, String bcUrl, RequestTypeEnum type) throws ControlSubSystemException {
        String url = data.getString("url");
        String goodsHash = data.getString("goodsHash");
        String queryPermissions = data.getString("queryPermissions");

        // 1.向权限管理子系统发送请求，接收到相关权限信息，并鉴权(不需要异步)
        AMSystemInfo amSystemInfo = authorityModule.query(client,prefix,type.getRequestCode());
        if (amSystemInfo.getStatus() == 0){
            logger.info("AuthorityVerifyError({})", amSystemInfo.getMessage());
            throw new ControlSubSystemException(amSystemInfo.getMessage());
        }

        // 2.向解析结果验证子系统、标识管理系统发送对应的json数据（异步）
        CompletableFuture<NormalMsg> dhtAnswer = null;
        CompletableFuture<NormalMsg> bcAnswer = null;
        switch (type) {
            case REQUEST_TYPE_REGISTER:
                dhtAnswer = dhtModule.register(identity,prefix,url,dhtUrl,type.getRequestCode());
                bcAnswer = blockchainModule.register(identity,goodsHash,url,bcUrl,queryPermissions);
                break;
            case REQUEST_TYPE_UPDATE:
                dhtAnswer = dhtModule.update(identity,prefix,url,dhtUrl,type.getRequestCode());
                bcAnswer = blockchainModule.update(identity,goodsHash,url,bcUrl,queryPermissions);
                break;
            default:
        }
        CompletableFuture.allOf(dhtAnswer, bcAnswer).join();

        // 3.检查注册/更新是否成功
        try {
            checkRequestStatus(dhtAnswer, bcAnswer);
        } catch (InterruptedException | ExecutionException e) {
            switch (type) {
                case REQUEST_TYPE_REGISTER:
                    throw new ControlSubSystemException("注册标识失败 " + e.getMessage());
                case REQUEST_TYPE_UPDATE:
                    throw new ControlSubSystemException("更新标识失败 " + e.getMessage());
                default:
            }
        }
    }

    @Override
    public void deleteHandle(String client, String identity, String prefix, String dhtUrl, String bcUrl, RequestTypeEnum type) throws ControlSubSystemException {
        //1.向权限管理子系统发送请求，接收到相关权限信息，并鉴权(不需要异步)
        AMSystemInfo amSystemInfo = authorityModule.query(client,prefix,type.getRequestCode());
        if (amSystemInfo.getStatus() == 0){
            logger.info("AuthorityVerifyError({})", amSystemInfo.getMessage());
            throw new ControlSubSystemException(amSystemInfo.getMessage());
        }

        // 2.向解析结果验证子系统、标识管理系统发送对应的json数据（异步）
        CompletableFuture<NormalMsg> dhtAnswer = dhtModule.delete(identity,prefix,dhtUrl,type.getRequestCode());
        CompletableFuture<NormalMsg> bcAnswer = blockchainModule.delete(identity,bcUrl);
        CompletableFuture.allOf(dhtAnswer, bcAnswer).join();

        // 3.检查注册/更新是否成功
        try {
            checkRequestStatus(dhtAnswer, bcAnswer);
        } catch (InterruptedException | ExecutionException e) {
            throw new ControlSubSystemException("删除标识失败 " + e.getMessage());
        }
    }

    @Override
    public QueryResult queryHandle(String client, String identity, String prefix, String domainPrefix, String dhtUrl, String bcUrl, String bcQueryOwner) throws ControlSubSystemException {
        boolean isCrossDomain = false;

        // 1.进行跨域解析判断
        isCrossDomain = !domainPrefix.equals(ownDomainPrefix);

        // 2.向标识管理子系统发送请求获得url向解析结果验证子系统发送请求获得两个摘要信息
        CompletableFuture<IMSystemInfo> dhtAnswer = dhtModule.query(identity,prefix,dhtUrl,isCrossDomain);
        CompletableFuture<RVSystemInfo> bcAnswer = blockchainModule.query(identity,bcUrl);
        CompletableFuture.allOf(dhtAnswer, bcAnswer).join();
        try {
            checkRequestStatus(dhtAnswer, bcAnswer);
        } catch (InterruptedException | ExecutionException e) {
            throw new ControlSubSystemException("解析标识失败 " + e.getMessage());
        }

        // 3.提取摘要信息和权限信息
        try {
            String queryPermission = bcAnswer.get().getPermission();
            String goodsHashInBc = bcAnswer.get().getMappingDataHash();
            String urlHashInBc = bcAnswer.get().getUrlHash();
            String urlInDht = dhtAnswer.get().getMappingData();
            String nodeId = dhtAnswer.get().getNodeID();

            checkQueryPermission(client,prefix,bcQueryOwner,queryPermission);
            checkUrlHash(urlInDht,urlHashInBc);
            checkGoodsHash(urlInDht,goodsHashInBc);

            QueryResult queryResult = new QueryResult();
            queryResult.setUrl(urlInDht);
            queryResult.setNodeID(nodeId);
            JSONObject tmpJson = new JSONObject();
            tmpJson.put("goodsInfo", "test success");
            String data  = tmpJson.toString();
            queryResult.setGoodsInfo(EncDecUtil.sMEncrypt(data));
            return queryResult;
        } catch (InterruptedException | ExecutionException e) {
            throw new ControlSubSystemException("解析标识失败 " + e.getMessage());
        }

    }

    @Override
    public AllPrefixIdAnswer queryAllIdByPrefix(String prefix, String client, String matchString, String bcUrl) throws ControlSubSystemException {
        AMSystemInfo amSystemInfo = authorityModule.query(client,prefix,1);
        if (amSystemInfo.getStatus() == 0){
            logger.info("AuthorityVerifyError({})", amSystemInfo.getMessage());
            throw new ControlSubSystemException(amSystemInfo.getMessage());
        }

        AllPrefixIdAnswer allPrefixIdAnswer = blockchainModule.prefixQuery(prefix,bcUrl,matchString);
        if (allPrefixIdAnswer.getStatus() == 0){
            logger.info(allPrefixIdAnswer.getMessage());
            throw new ControlSubSystemException(allPrefixIdAnswer.getMessage());
        }

        return allPrefixIdAnswer;
    }


    @Override
    public int bulkRegister(BulkRegister bulkRegister, String dhtUrl, String bcUrl) throws Exception {
        int idCount = bulkRegister.getData().size();
        int number = 0;
        String client = bulkRegister.getClient();

        do {
            String identification = bulkRegister.getData().get(number).getString("identification");
            String url = bulkRegister.getData().get(number).getString("url");
            String goodsHash = bulkRegister.getData().get(number).getString("goodsHash");
            String queryPermissions = bulkRegister.getData().get(number).getString("queryPermissions");
            JSONObject data = new JSONObject();
            data.put("url", url);
            data.put("goodsHash", goodsHash);
            data.put("queryPermissions", queryPermissions);
            String prefix = InfoFromClient.getPrefix(identification);

            try {
                registerAndUpdateHandle(client,identification,prefix,data,dhtUrl,bcUrl,RequestTypeEnum.REQUEST_TYPE_REGISTER);
            }catch (Exception e) {
                String errStr = String.format("已成功注册%d个标识，第%d个标识注册出错，出错原因: %s", number, number+1, e.getMessage());
                throw new Exception(errStr);
            }

            number++;
        } while (number != idCount);

        return idCount;
    }

    private void checkRequestStatus(CompletableFuture<? extends NormalMsg> dhtAnswer, CompletableFuture<? extends NormalMsg> bcAnswer) throws InterruptedException, ExecutionException, ControlSubSystemException {
        int bcStatus = bcAnswer != null ? bcAnswer.get().getStatus() : 0;
        int dhtStatus = dhtAnswer != null ? dhtAnswer.get().getStatus() : 0;
        String bcMessage = bcAnswer != null ? bcAnswer.get().getMessage() : null;
        String dhtMessage = dhtAnswer != null ? dhtAnswer.get().getMessage() : null;

        if (bcStatus == 0 || dhtStatus == 0) {
            String errorMessageBc = getErrorMessage(bcStatus,MODULE_TYPE_BC,bcMessage);
            String errorMessageDht = getErrorMessage(dhtStatus,MODULE_TYPE_DHT,dhtMessage);
            throw new ControlSubSystemException(errorMessageBc + errorMessageDht);
        }
    }

    private String getErrorMessage(int status, int moduleType, String message) {
        if (status == 1) {
            return null;
        }
        switch (moduleType) {
            case MODULE_TYPE_BC:
                return " 区块链节点错误！错误信息:" + message;
            case MODULE_TYPE_DHT:
                return " DHT节点错误信息！错误信息:" + message;
            default:
                return null;
        }
    }

    private void checkQueryPermission(String client, String prefix, String bcQueryOwner, String queryPermission) throws ControlSubSystemException {
        String owner = "";
        final String onlyOwner = "0";
        NormalMsg normalMsg = blockchainModule.queryOwnerByPrefix(prefix, bcQueryOwner);
        if (normalMsg.getStatus() == 0) {
            throw new ControlSubSystemException(normalMsg.getMessage());
        }
        else {
            owner = normalMsg.getMessage();
        }
        if (queryPermission.equals(onlyOwner) && !client.equals(owner)) {
            logger.info("用户没有查看该标识的权限！！！");
            throw new ControlSubSystemException("用户" + client + "没有查看该标识的权限！！！");
        }
    }

    private void checkUrlHash(String urlInDht, String urlHashInBc) throws ControlSubSystemException {
        urlInDht = urlInDht.replace(" ", "");
        String urlHash = HashUtil.SM3Hash(urlInDht);

        if(!urlHashInBc.equals(urlHash)) {
            logger.info(ErrorMessageEnum.URLHASH_VERIFY_ERROR.getMsg());
            throw new ControlSubSystemException(ErrorMessageEnum.URLHASH_VERIFY_ERROR.getMsg());
        }
    }

    private void checkGoodsHash(String urlInDht, String goodsHashInBc) throws ControlSubSystemException {
        ComQueryInfo comQueryInfo = comInfoModule.query(urlInDht);

        if(comQueryInfo.getStatus() == 0){
            logger.info(comQueryInfo.getMessage());
            throw new ControlSubSystemException(comQueryInfo.getMessage());
        }
        String goodsHash = HashUtil.SM3Hash(comQueryInfo.getInformation().toString());

        if (!goodsHashInBc.equals(goodsHash)) {
            logger.info(ErrorMessageEnum.GOODSHASH_VERIFY_ERROR.getMsg());
            throw new ControlSubSystemException(ErrorMessageEnum.GOODSHASH_VERIFY_ERROR.getMsg());
        }
    }
}
