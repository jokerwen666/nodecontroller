package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.communication.AuthorityModule;
import com.hust.nodecontroller.communication.BlockchainModule;
import com.hust.nodecontroller.communication.ComInfoModule;
import com.hust.nodecontroller.communication.DhtModule;
import com.hust.nodecontroller.enums.IdentityTypeEnum;
import com.hust.nodecontroller.errorhandle.BCErrorHandle;
import com.hust.nodecontroller.errorhandle.DhtErrorHandle;
import com.hust.nodecontroller.infostruct.*;
import com.hust.nodecontroller.enums.AuthorityResultEnum;
import com.hust.nodecontroller.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName ControlProcess
 * @date 2020.10.07 14:25
 */

@Component
public class ControlProcessImpl implements ControlProcess{

    private final DhtModule dhtModule;
    private final BlockchainModule blockchainModule;
    private final AuthorityModule authorityModule;
    private final ComInfoModule comInfoModule;
    private static final Logger logger = LoggerFactory.getLogger(ControlProcessImpl.class);
    public final String domainPrefix;


    @Autowired
    public ControlProcessImpl(ApplicationArguments applicationArguments, DhtModule dhtModule, BlockchainModule blockchainModule, AuthorityModule authorityModule, ComInfoModule comInfoModule, BCErrorHandle bcErrorHandle, DhtErrorHandle dhtErrorHandle, ComInfoModule comInfoModule1) {
        this.dhtModule = dhtModule;
        this.blockchainModule = blockchainModule;
        this.authorityModule = authorityModule;
        this.domainPrefix = applicationArguments.getOptionValues("domainPrefix").get(0);
        this.comInfoModule = comInfoModule1;
    }


    @Override
    public void enterpriseHandle(InfoFromClient infoFromClient, String dhtUrl, String bcUrl, int type, boolean isTrust) throws Exception {

        String client = infoFromClient.getClient(); //请求发送的企业名称
        String identity = infoFromClient.getIdentification(); //请求标识
        String prefix = InfoFromClient.getPrefix(identity); //标识前缀
        JSONObject data = infoFromClient.getData(); //注册信息

        String url = null;
        String goodsHash = null;
        String queryPermissions = null;

        if(type == 2 || type == 8){
            url = data.getString("url");
            // 更新操作，url为空，默认使用
             if (type == 2 && url.equals("")) {
                String queryUrl = dhtUrl.replace("modify", "resolve");
                CompletableFuture<IMSystemInfo> dhtInfo = dhtModule.query(identity,prefix,queryUrl, false);
                url = dhtInfo.get().getMappingData();
            }
            goodsHash = data.getString("goodsHash").toLowerCase();
            queryPermissions = data.getString("queryPermissions");

        }

        //1.向权限管理子系统发送请求，接收到相关权限信息，并鉴权(不需要异步)，如果之前已经权限校验过此时不需要校验
        if (!isTrust) {
            AMSystemInfo amSystemInfo = authorityModule.query(client,prefix,type);
            if (amSystemInfo.getStatus() == 0){
                logger.info("AuthorityVerifyError({})", amSystemInfo.getMessage());
                throw new Exception(amSystemInfo.getMessage());
            }
        }

        //2.向解析结果验证子系统、标识管理系统发送对应的json数据（异步）
        CompletableFuture<NormalMsg> dhtInfo = null;
        CompletableFuture<NormalMsg> bcInfo = null;
        if (type == 8){
            dhtInfo = dhtModule.register(identity,prefix,url,dhtUrl,type);
            bcInfo = blockchainModule.register(identity,goodsHash,url,bcUrl,queryPermissions);
        }

        else if(type == 4){
            dhtInfo = dhtModule.delete(identity,prefix,dhtUrl,type);
            bcInfo = blockchainModule.delete(identity,bcUrl);
        }
        else if (type == 2){
            dhtInfo = dhtModule.update(identity,prefix,url,dhtUrl,type);
            bcInfo = blockchainModule.update(identity,goodsHash,url,bcUrl,queryPermissions);
        }

        CompletableFuture.allOf(dhtInfo, bcInfo).join();

        assert bcInfo != null;
        assert dhtInfo != null;

        if(bcInfo.get().getStatus() == 0 && dhtInfo.get().getStatus() != 0){
            logger.info("BlockchainErrorMsg({})", bcInfo.get().getMessage());
            String errStr = "区块链节点错误信息(" + bcInfo.get().getMessage() + ") ";
            throw new Exception(errStr);
        }

        if(bcInfo.get().getStatus() != 0 && dhtInfo.get().getStatus() == 0){
            logger.info("DHTErrorMsg({})", dhtInfo.get().getMessage());
            String errStr = "DHT节点错误信息(" + dhtInfo.get().getMessage() + ")";
            throw new Exception(errStr);
        }

        if(bcInfo.get().getStatus() == 0 && dhtInfo.get().getStatus() == 0){
            logger.info("BlockchainErrorMsg({}), DHTErrorMsg({})", bcInfo.get().getMessage(), dhtInfo.get().getMessage());
            String errStr = "区块链节点错误信息(" + bcInfo.get().getMessage() + ") " + "DHT节点错误信息(" + dhtInfo.get().getMessage() + ")";
            throw new Exception(errStr);
        }

    }


    @Override
    public QueryResult userHandle(String identity, String client, String dhtUrl, String bcUrl, String bcQueryOwner, String encType) throws Exception {
        CalStateUtil.dhtQueryCount++;
        CalStateUtil.timeoutCount++;
        String prefix = InfoFromClient.getPrefix(identity);
        String domainPrefix_ = InfoFromClient.getDomainPrefix(identity);
        boolean crossDomain_flag = false;
        String owner = "";
        long beginTime = System.nanoTime();

        //1.进行跨域解析判断
        crossDomain_flag = !domainPrefix_.equals(domainPrefix);

        //2.向标识管理子系统发送请求获得url向解析结果验证子系统发送请求获得两个摘要信息
        CompletableFuture<IMSystemInfo> dhtInfo = dhtModule.query(identity,prefix,dhtUrl,crossDomain_flag);
        CompletableFuture<RVSystemInfo> bcInfo = blockchainModule.query(identity,bcUrl);
        CompletableFuture<NormalMsg> authorityInfo = blockchainModule.queryOwnerByPrefix(prefix, bcQueryOwner);
        CompletableFuture.allOf(dhtInfo, bcInfo,authorityInfo).join();


        if(bcInfo.get().getStatus() == 0 && dhtInfo.get().getStatus() != 0){
            logger.info("BlockchainErrorMsg({})", bcInfo.get().getMessage());
            String errStr = "区块链节点错误信息(" + bcInfo.get().getMessage() + ")";
            throw new Exception(errStr);
        }

        if(bcInfo.get().getStatus() != 0 && dhtInfo.get().getStatus() == 0){
            logger.info("DHTErrorMsg({})", dhtInfo.get().getMessage());
            String errStr = "DHT节点错误信息(" + dhtInfo.get().getMessage() + ")";
            throw new Exception(errStr);
        }

        if(bcInfo.get().getStatus() == 0 && dhtInfo.get().getStatus() == 0){
            logger.info("BlockchainErrorMsg({}), DHTErrorMsg({})", bcInfo.get().getMessage(), dhtInfo.get().getMessage());
            String errStr = "区块链节点错误信息(" + bcInfo.get().getMessage() + ") " + "DHT节点错误信息(" + dhtInfo.get().getMessage() + ")";
            throw new Exception(errStr);
        }

        //3 根据前缀查找其所有者并校验
        if (authorityInfo.get().getStatus() == 0) {
            throw new Exception(authorityInfo.get().getMessage());
        }
        else owner = authorityInfo.get().getMessage();
        String permission = bcInfo.get().getPermission();
        if (permission.equals("0") && !client.equals(owner)) {
            logger.info("用户没有查看该标识的权限！！！");
            throw new Exception("用户" + client + "没有查看该标识的权限！！！");
        }

        //4.防篡改检验(url校验+goodsHash校验)(可以更新为异步)
        String url = dhtInfo.get().getMappingData();
        url = url.replace(" ", "");
        String urlHash_ = EncDecUtil.sMHash(url);

        String urlHash = bcInfo.get().getUrlHash();
        String goodsHash = bcInfo.get().getMappingDataHash();

        if(!urlHash.equals(urlHash_)) {
            logger.info(AuthorityResultEnum.URLHASH_VERIFY_ERROR.getMsg());
            throw new Exception(AuthorityResultEnum.URLHASH_VERIFY_ERROR.getMsg());
        }


        ComQueryInfo comQueryInfo = comInfoModule.query(url);

        if(comQueryInfo.getStatus() == 0){
            logger.info(comQueryInfo.getMessage());
            throw new Exception(comQueryInfo.getMessage());
        }
        String preHash = comQueryInfo.getInformation().toString();
        String goodsHash_ = EncDecUtil.sMHash(preHash);

        if (!goodsHash.equals(goodsHash_)) {
            logger.info(AuthorityResultEnum.GOODSHASH_VERIFY_ERROR.getMsg());
            throw new Exception(AuthorityResultEnum.GOODSHASH_VERIFY_ERROR.getMsg());
        }

        QueryResult queryResult = new QueryResult();
        queryResult.setUrl(url);
        queryResult.setNodeID(dhtInfo.get().getNodeID());

        String data = comQueryInfo.getInformation().toString();
        if ("none".equals(encType)) {
            queryResult.setGoodsInfo(data);
        }
        else {
            queryResult.setGoodsInfo(EncDecUtil.sMEncrypt(data));
        }

        long endTime = System.nanoTime();
        CalStateUtil.queryTimeout += (endTime-beginTime)/1000000;
        return queryResult;
    }

    @Override
    public IdentityInfo identityHandle(InfoFromClient infoFromClient, String bcUrl) throws Exception {
        String prefix = infoFromClient.getOrgPrefix();
        String client = infoFromClient.getClient();
        String matchString = infoFromClient.getMatchString();

        AMSystemInfo amSystemInfo = authorityModule.query(client,prefix,1);
        if (amSystemInfo.getStatus() == 0){
            logger.info("AuthorityVerifyError({})", amSystemInfo.getMessage());
            throw new Exception(amSystemInfo.getMessage());
        }

        IdentityInfo identityInfo = blockchainModule.prefixQuery(prefix,bcUrl,matchString);
        if (identityInfo.getStatus() == 0){
            logger.info(identityInfo.getMessage());
            throw new Exception(identityInfo.getMessage());
        }

        return identityInfo;
    }

    @Override
    public SinglePageInfo singlePageHandle(InfoFromClient infoFromClient, String bcPrefixQuery, String dhtUrl) throws Exception {
        String prefix = infoFromClient.getOrgPrefix();
        String client = infoFromClient.getClient();
        String matchString = infoFromClient.getMatchString();
        String txid = infoFromClient.getTxid();

        AMSystemInfo amSystemInfo = authorityModule.query(client,prefix,1);
        if (amSystemInfo.getStatus() == 0){
            logger.info("AuthorityVerifyError({})", amSystemInfo.getMessage());
            throw new Exception(amSystemInfo.getMessage());
        }

        // 有匹配字符转为单个标识查询
        if (matchString != "") {
            if (IdTypeJudgeUtil.typeJudge(matchString).equals(IdentityTypeEnum.IDENTITY_TYPE_NOT_SUPPORT)) {
                SinglePageInfo singlePageInfo = new SinglePageInfo();
                singlePageInfo.setStatus(0);
                singlePageInfo.setMessage("Matching String is invalid!");
                return singlePageInfo;
            }

            CompletableFuture<RVSystemInfo> bcInfo = blockchainModule.query(matchString, "http://39.107.238.25:8686/api/queryIdentifier");
            String urlHash = bcInfo.get().getUrlHash();
            String goodsHash = bcInfo.get().getMappingDataHash();
            String permission = bcInfo.get().getPermission();
            if (permission.equals("1")) {
                permission = "all";
            } else {
                permission = "only";
            }

            JSONObject jsonObject = new JSONObject();
            List<JSONObject> identityList = new ArrayList<>();
            jsonObject.put("identity", matchString);
            jsonObject.put("urlHash", urlHash);
            jsonObject.put("goodsHash", goodsHash);
            jsonObject.put("queryAuthority", permission);
            identityList.add(jsonObject);
            SinglePageInfo singlePageInfo = new SinglePageInfo();
            singlePageInfo.setCount(1);
            singlePageInfo.setIdentityList(identityList);
            singlePageInfo.setStatus(1);
            singlePageInfo.setMessage("Query Single Page Success!");
            return singlePageInfo;
        }

        JSONObject callJson = new JSONObject();
        callJson.put("orgname", prefix);
        callJson.put("type", 5);
        IdentityRankInfo identityRankInfo = PostRequestUtil.queryIdRankByPrefix(dhtUrl,callJson);
        int totalCount = identityRankInfo.getIdNums();


        SinglePageInfo singlePageInfo = blockchainModule.singlePageQuery(prefix, bcPrefixQuery, matchString, txid, totalCount);
        if (singlePageInfo.getStatus() == 0) {
            logger.info(singlePageInfo.getMessage());
            throw new Exception(singlePageInfo.getMessage());
        }
        return singlePageInfo;
    }


    @Override
    public int bulkRegister(BulkRegister bulkRegister, String dhtUrl, String bcUrl) throws Exception {
        int idCount = bulkRegister.getData().size();
        int number = 0;
        String client = bulkRegister.getClient();
        String prefix = bulkRegister.getPrefix();

        AMSystemInfo amSystemInfo = authorityModule.query(client,prefix,8);
        if (amSystemInfo.getStatus() == 0){
            logger.info("AuthorityVerifyError({})", amSystemInfo.getMessage());
            throw new Exception(amSystemInfo.getMessage());
        }

        do {
            String identification = bulkRegister.getData().getJSONObject(number).getString("identification");
            String idPrefix = InfoFromClient.getPrefix(identification);
            if (!idPrefix.equals(prefix)) {
                String errStr = String.format("已成功注册%d个标识，第%d个标识注册出错，出错原因: %s", number, number+1, "尝试注册未持有的标识！");
                throw new Exception(errStr);
            }
            String url = bulkRegister.getData().getJSONObject(number).getString("url");
            String goodsHash = bulkRegister.getData().getJSONObject(number).getString("goodsHash");
            String queryPermissions = bulkRegister.getData().getJSONObject(number).getString("queryPermissions");
            JSONObject data = new JSONObject();
            data.put("url", url);
            data.put("goodsHash", goodsHash);
            data.put("queryPermissions", queryPermissions);
            InfoFromClient infoFromClient = new InfoFromClient();
            infoFromClient.setClient(client);
            infoFromClient.setData(data);
            infoFromClient.setIdentification(identification);
            try {
                enterpriseHandle(infoFromClient,dhtUrl,bcUrl,8, true);
                CalStateUtil.registerCount++;
                CalStateUtil.totalCount++;
                number++;
            }catch (Exception e) {
                String errStr = String.format("已成功注册%d个标识，第%d个标识注册出错，出错原因: %s", number, number+1, e.getMessage());
                throw new Exception(errStr);
            }
        } while (number != idCount);
        return idCount;
    }

    @Override
    public BulkInfo bulkQuery(JSONArray jsonArray, String url) {
        int idCount = jsonArray.size();
        CalStateUtil.queryCount += idCount;
        IndustryQueryUtil.queryCount += idCount;
        int number = 0;
        StringBuilder identities = new StringBuilder();

        do {
            JSONObject jsonObject = jsonArray.getJSONObject(number);
            identities.append(jsonObject.getString("identity"));
            identities.append(';');
            number++;
        } while (number != idCount);

        return dhtModule.bulkQuery(identities,url);

    }

}
