package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.communication.AuthorityModule;
import com.hust.nodecontroller.communication.BlockchainModule;
import com.hust.nodecontroller.communication.ComInfoModule;
import com.hust.nodecontroller.communication.DhtModule;
import com.hust.nodecontroller.errorhandle.BCErrorHandle;
import com.hust.nodecontroller.errorhandle.DhtErrorHandle;
import com.hust.nodecontroller.infostruct.*;
import com.hust.nodecontroller.enums.AuthorityResultEnum;
import com.hust.nodecontroller.utils.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        String prefix = infoFromClient.getPrefix(); //标识前缀
        JSONObject data = infoFromClient.getData();

        String url = null;
        String goodsHash = null;

        if(type == 2 || type == 8){
            url = data.getString("url");
            goodsHash = data.getString("goodsHash");
        }

        //1.向权限管理子系统发送请求，接收到相关权限信息，并鉴权
        Future<AMSystemInfo> amSystemInfo = authorityModule.query(client,prefix,type);

        while (!amSystemInfo.isDone()){
            if (amSystemInfo.isDone())
                break;
        }
        if (amSystemInfo.get().getStatus() == 0){
            logger.info("AuthorityVerifyError({})", amSystemInfo.get().getMessage());
            throw new Exception(amSystemInfo.get().getMessage());
        }

        //2.向解析结果验证子系统、标识管理系统发送对应的json数据
        Future<NormalMsg> dhtFlag = null;
        Future<NormalMsg> bcFlag = null;
        if (type == 8){
            dhtFlag = dhtModule.register(identity,prefix,url,dhtUrl,type);
            bcFlag = blockchainModule.register(identity,goodsHash,url,bcUrl);
        }

        else if(type == 4){
            dhtFlag = dhtModule.delete(identity,prefix,dhtUrl,type);
            bcFlag = blockchainModule.delete(identity,bcUrl);
        }
        else if (type == 2){
            dhtFlag = dhtModule.update(identity,prefix,url,dhtUrl,type);
            bcFlag = blockchainModule.update(identity,goodsHash,url,bcUrl);
        }

        //4.判断是否完成写入
        while (true) {
            if(dhtFlag.isDone() && bcFlag.isDone()) {
                break;
            }
        }

        if(bcFlag.get().getStatus() == 0 && dhtFlag.get().getStatus() != 0){
            //bcErrorHandle.errorHandle(type,identity);
            logger.info("BlockchainErrorMsg({})", bcFlag.get().getMessage());
            throw new Exception(bcFlag.get().getMessage());
        }

        if(bcFlag.get().getStatus() != 0 && dhtFlag.get().getStatus() == 0){
            //dhtErrorHandle.errorHandle(type,identity,prefix);
            logger.info("DHTErrorMsg({})", dhtFlag.get().getMessage());
            throw new Exception(dhtFlag.get().getMessage());
        }

        if(bcFlag.get().getStatus() == 0 && dhtFlag.get().getStatus() == 0){
            logger.info("BlockchainErrorMsg({}), DHTErrorMsg({})", bcFlag.get().getMessage(), dhtFlag.get().getMessage());
            String errStr = "区块链节点错误信息(" + bcFlag.get().getMessage() + ") " + "DHT节点错误信息(" + dhtFlag.get().getMessage() + ")";
            throw new Exception(errStr);
        }

    }

    public QueryResult userHandle(InfoFromClient infoFromClient, String dhtUrl, String bcUrl) throws Exception {
        String identity = infoFromClient.getIdentification();
        String client = infoFromClient.getClient();
        String prefix = infoFromClient.getPrefix();
        Boolean crossDomain_flag = infoFromClient.getCrossDomain_flag();

        //1.进行跨域解析判断

        //2.向权限管理子系统发送请求，接收到相关权限信息
        Future<AMSystemInfo> amSystemInfo = authorityModule.query(client,prefix,1);

        //3.向标识管理子系统发送请求获得url\向解析结果验证子系统发送请求获得两个摘要信息
        Future<IMSystemInfo> dhtFlag = dhtModule.query(identity,prefix,dhtUrl,crossDomain_flag);
        Future<RVSystemInfo> bcFlag = blockchainModule.query(identity,bcUrl);

        //4.判断是否完成查询
        while (true) {
            if(dhtFlag.isDone() && bcFlag.isDone() && amSystemInfo.isDone()) {
                break;
            }
        }

        if (amSystemInfo.get().getStatus() == 0){
            logger.info("AuthorityVerifyError({})", amSystemInfo.get().getMessage());
            throw new Exception(amSystemInfo.get().getMessage());
        }

        if(bcFlag.get().getStatus() == 0 && dhtFlag.get().getStatus() != 0){
            logger.info("BlockchainErrorMsg({})", bcFlag.get().getMessage());
            String errStr = "区块链节点错误信息(" + bcFlag.get().getMessage() + ")";
            throw new Exception(errStr);
        }

        if(bcFlag.get().getStatus() != 0 && dhtFlag.get().getStatus() == 0){
            logger.info("DHTErrorMsg({})", dhtFlag.get().getMessage());
            String errStr = "DHT节点错误信息(" + dhtFlag.get().getMessage() + ")";
            throw new Exception(errStr);
        }

        if(bcFlag.get().getStatus() == 0 && dhtFlag.get().getStatus() == 0){
            logger.info("BlockchainErrorMsg({}), DHTErrorMsg({})", bcFlag.get().getMessage(), dhtFlag.get().getMessage());
            String errStr = "区块链节点错误信息(" + bcFlag.get().getMessage() + ") " + "DHT节点错误信息(" + dhtFlag.get().getMessage() + ")";
            throw new Exception(errStr);
        }

        //5.防篡改检验
        String url = dhtFlag.get().getMappingData();
        String urlHash_ = Integer.toHexString(HashUtil.apHash(url));
        String urlHash = bcFlag.get().getUrlHash();
        String goodsHash = bcFlag.get().getMappingDataHash();

        if(!urlHash.equals(urlHash_)) {
            logger.info(AuthorityResultEnum.URLHASH_VERIFY_ERROR.getMsg());
            throw new Exception(AuthorityResultEnum.URLHASH_VERIFY_ERROR.getMsg());
        }

        ComQueryInfo comQueryInfo = comInfoModule.query(url);

        if(comQueryInfo.getStatus() == 0){
            logger.info(comQueryInfo.getMessage());
            throw new Exception(comQueryInfo.getMessage());
        }

        String goodsHash_ = Integer.toHexString(HashUtil.apHash(comQueryInfo.getInformation()));

        if (!goodsHash.equals(goodsHash_)) {
            logger.info(AuthorityResultEnum.GOODSHASH_VERIFY_ERROR.getMsg());
            throw new Exception(AuthorityResultEnum.GOODSHASH_VERIFY_ERROR.getMsg());
        }

        QueryResult queryResult = new QueryResult();
        queryResult.setUrl(url);
        queryResult.setGoodsInfo(comQueryInfo.getInformation());
        queryResult.setNodeID(dhtFlag.get().getNodeID());

        return queryResult;
    }

    @Override
    public IdentityInfo identityHandle(InfoFromClient infoFromClient, String bcUrl) throws Exception {
        String prefix = infoFromClient.getOrgPrefix();
        String client = infoFromClient.getClient();

        Future<AMSystemInfo> amSystemInfo = authorityModule.query(client,prefix,1);

        while (!amSystemInfo.isDone()){
            if (amSystemInfo.isDone())
                break;
        }
        if (amSystemInfo.get().getStatus() == 0){
            logger.info("AuthorityVerifyError({})", amSystemInfo.get().getMessage());
            throw new Exception(amSystemInfo.get().getMessage());
        }

        IdentityInfo identityInfo = blockchainModule.prefixQuery(prefix,bcUrl);

        if (identityInfo.getStatus() == 0){
            logger.info(identityInfo.getMessage());
            throw new Exception(identityInfo.getMessage());
        }

        return identityInfo;
    }


}
