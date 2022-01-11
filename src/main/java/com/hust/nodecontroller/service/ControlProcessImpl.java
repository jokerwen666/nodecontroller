package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.communication.AuthorityModule;
import com.hust.nodecontroller.communication.BlockchainModule;
import com.hust.nodecontroller.communication.ComInfoModule;
import com.hust.nodecontroller.communication.DhtModule;
import com.hust.nodecontroller.errorhandle.BCErrorHandle;
import com.hust.nodecontroller.errorhandle.DhtErrorHandle;
import com.hust.nodecontroller.fnlencrypt.hashutils.ApHash;
import com.hust.nodecontroller.fnlencrypt.hashutils.HashUtils;
import com.hust.nodecontroller.fnlencrypt.hashutils.SM3Hash;
import com.hust.nodecontroller.infostruct.*;
import com.hust.nodecontroller.enums.AuthorityResultEnum;
import com.hust.nodecontroller.utils.CalStateUtil;
import com.hust.nodecontroller.utils.EncDecUtil;
import com.hust.nodecontroller.utils.HashUtil;
import com.hust.nodecontroller.utils.IndustryQueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

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
    private final BCErrorHandle bcErrorHandle;
    private final DhtErrorHandle dhtErrorHandle;
    private static final Logger logger = LoggerFactory.getLogger(ControlProcessImpl.class);
    @Value("${domain.prefix}")
    public static String domainPrefix;

    @Autowired
    public ControlProcessImpl(DhtModule dhtModule, BlockchainModule blockchainModule, AuthorityModule authorityModule, ComInfoModule comInfoModule, BCErrorHandle bcErrorHandle, DhtErrorHandle dhtErrorHandle) {
        this.dhtModule = dhtModule;
        this.blockchainModule = blockchainModule;
        this.authorityModule = authorityModule;
        this.comInfoModule = comInfoModule;
        this.bcErrorHandle = bcErrorHandle;
        this.dhtErrorHandle = dhtErrorHandle;
    }

    public void enterpriseHandle(InfoFromClient infoFromClient, String dhtUrl, String bcUrl, int type) throws Exception {

        String client = infoFromClient.getClient(); //请求发送的企业名称
        String identity = infoFromClient.getIdentification(); //请求标识
        String prefix = InfoFromClient.getPrefix(identity); //标识前缀
        JSONObject data = infoFromClient.getData(); //注册信息

        String url = null;
        String goodsHash = null;
        String queryPermissions = null;

        if(type == 2 || type == 8){
            url = data.getString("url");
            goodsHash = data.getString("goodsHash");
            queryPermissions = data.getString("queryPermissions");

        }

        //1.向权限管理子系统发送请求，接收到相关权限信息，并鉴权(不需要异步)
        AMSystemInfo amSystemInfo = authorityModule.query(client,prefix,type);
        if (amSystemInfo.getStatus() == 0){
            logger.info("AuthorityVerifyError({})", amSystemInfo.getMessage());
            throw new Exception(amSystemInfo.getMessage());
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
            dhtErrorHandle.errorHandle(type,identity,prefix);
            logger.info("BlockchainErrorMsg({})", bcInfo.get().getMessage());
            String errStr = "区块链节点错误信息(" + bcInfo.get().getMessage() + ") ";
            throw new Exception(errStr);
        }

        if(bcInfo.get().getStatus() != 0 && dhtInfo.get().getStatus() == 0){
            bcErrorHandle.errorHandle(type,identity);
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

    public QueryResult userHandle(String identity, String client, String dhtUrl, String bcUrl, String bcQueryOwner) throws Exception {

        String prefix = InfoFromClient.getPrefix(identity);
        String domainPrefix_ = InfoFromClient.getDomainPrefix(identity);
        boolean crossDomain_flag = false;

        //1.进行跨域解析判断
        crossDomain_flag = !domainPrefix_.equals(domainPrefix);

        //2.向标识管理子系统发送请求获得url向解析结果验证子系统发送请求获得两个摘要信息
        CompletableFuture<IMSystemInfo> dhtInfo = dhtModule.query(identity,prefix,dhtUrl,crossDomain_flag);
        CompletableFuture<RVSystemInfo> bcInfo = blockchainModule.query(identity,bcUrl);

        CompletableFuture.allOf(dhtInfo, bcInfo).join();

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
        String owner = "";
        NormalMsg normalMsg = blockchainModule.queryOwnerByPrefix(prefix, bcQueryOwner);
        if (normalMsg.getStatus() == 0) {
            throw new Exception(normalMsg.getMessage());
        }
        else owner = normalMsg.getMessage();

        String permission = bcInfo.get().getPermission();
        if (permission.equals("0") && !client.equals(owner)) {
            logger.info("用户没有查看该标识的权限！！！");
            throw new Exception("用户" + client + "没有查看该标识的权限！！！");
        }

        //4.防篡改检验(url校验+goodsHash校验)(可以更新为异步)
        String url = dhtInfo.get().getMappingData();
        url = url.replace(" ", "");

        String urlHash_ = HashUtil.SM3Hash(url);
        String urlHash = bcInfo.get().getUrlHash();
        String goodsHash = bcInfo.get().getMappingDataHash();

        if(!urlHash.equals(urlHash_)) {
            logger.info(AuthorityResultEnum.URLHASH_VERIFY_ERROR.getMsg());
            throw new Exception(AuthorityResultEnum.URLHASH_VERIFY_ERROR.getMsg());
        }


//        ComQueryInfo comQueryInfo = comInfoModule.query(url);
//
//        if(comQueryInfo.getStatus() == 0){
//            logger.info(comQueryInfo.getMessage());
//            throw new Exception(comQueryInfo.getMessage());
//        }
//        String goodsHash_ = HashUtil.SM3Hash(comQueryInfo.getInformation().toString());
//
//        if (!goodsHash.equals(goodsHash_)) {
//            logger.info(AuthorityResultEnum.GOODSHASH_VERIFY_ERROR.getMsg());
//            throw new Exception(AuthorityResultEnum.GOODSHASH_VERIFY_ERROR.getMsg());
//        }

        QueryResult queryResult = new QueryResult();
        queryResult.setUrl(url);
        queryResult.setNodeID(dhtInfo.get().getNodeID());

//        queryResult.setGoodsInfo(comQueryInfo.getInformation());
        JSONObject tmpJson = new JSONObject();
        tmpJson.put("goodsInfo", "test success");
        String data  = tmpJson.toString();
        queryResult.setGoodsInfo(EncDecUtil.sMEncrypt(data));

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
    public BulkInfo bulkRegister(JSONArray jsonArray, String dhtUrl, String bcUrl){
        int idCount = jsonArray.size();
        int number = 0;
        StringBuilder identities = new StringBuilder();
        StringBuilder mappingData = new StringBuilder();

        do {
            JSONObject jsonObject = jsonArray.getJSONObject(number);
            identities.append(jsonObject.getString("identity"));
            identities.append(';');
            mappingData.append(jsonObject.getString("url"));
            mappingData.append(';');
            number++;
        } while (number != idCount);

        /**
         * 添加blockchain注册信息，并将注册结果返回值合并
         */
        return dhtModule.bulkRegister(identities,mappingData,dhtUrl);

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
