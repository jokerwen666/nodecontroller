package com.hust.nodecontroller.communication;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.enums.AuthorityResultEnum;
import com.hust.nodecontroller.infostruct.AMSystemInfo;
import com.hust.nodecontroller.infostruct.NormalMsg;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName AuthorityModule
 * @date 2020.10.18 12:42
 */

@Component
public class AuthorityModule implements sendInfoToModule{

    //鉴权子系统url
    @Value("${am.save.url}")
    String amSaveUrl;
    @Value("${am.delete.url}")
    String amDeleteUrl;
    @Value("${am.update.url}")
    String amUpdateUrl;
    @Value("${am.query.url}")
    String amQueryUrl;

    private static final Logger logger = LoggerFactory.getLogger(AuthorityModule.class);

    public NormalMsg register(String erp_name, String prefix, String key, String authority, String owner) throws Exception{
        return registerAndUpdate(erp_name, prefix, key, authority, owner, amSaveUrl);
    }

    public NormalMsg update(String erp_name, String prefix, String key, String authority, String owner) throws Exception{
        return registerAndUpdate(erp_name, prefix, key, authority, owner, amUpdateUrl);
    }

    public AMSystemInfo query(String client, String prefix, int type){
        JSONObject jsonToAMSystem = new JSONObject();
        jsonToAMSystem.put("peer_name","peer0");
        jsonToAMSystem.put("erp_name", client);
        jsonToAMSystem.put("identity_prefix", prefix);
        AMSystemInfo amSystemInfo = new AMSystemInfo();
        try {
            amSystemInfo = PostRequestUtil.getAMQueryResponse(amQueryUrl,jsonToAMSystem);

            if (amSystemInfo.getStatus() == 0){
                return amSystemInfo;
            }

            int authority = Integer.parseInt(amSystemInfo.getAuthority(), 2);

            if((authority & type) == 0){
                amSystemInfo.setStatus(0);
                amSystemInfo.setMessage(AuthorityResultEnum.OPERATION_AUTHORITY_VERIFY_ERROR.getMsg());
                return amSystemInfo;
            }
            return amSystemInfo;
        }catch (Exception e){
            amSystemInfo.setStatus(0);
            amSystemInfo.setMessage(AuthorityResultEnum.OPERATION_AUTHORITY_VERIFY_ERROR.getMsg());
            return amSystemInfo;
        }
    }

    public NormalMsg delete(String erp_name, String prefix) throws Exception{
        JSONObject jsonToAMSystem = new JSONObject();
        jsonToAMSystem.put("peer_name", "peer0");
        jsonToAMSystem.put("erp_name", erp_name);
        jsonToAMSystem.put("identity_prefix", prefix);

        NormalMsg normalMsg = PostRequestUtil.getNormalResponse(amDeleteUrl,jsonToAMSystem);

        if (normalMsg.getStatus() == 0){
            logger.info("Error({})",normalMsg.getMessage());
            throw  new Exception(normalMsg.getMessage());
        }

        return normalMsg;
    }

    public NormalMsg registerAndUpdate(String erp_name, String prefix, String key, String authority, String owner, String toUrl) throws Exception{
        JSONObject jsonToAMSystem = new JSONObject();
        jsonToAMSystem.put("peer_name", "peer0");
        jsonToAMSystem.put("erp_name", erp_name);
        jsonToAMSystem.put("identity_prefix", prefix);
        jsonToAMSystem.put("public_key", key);
        jsonToAMSystem.put("authority", authority);
        jsonToAMSystem.put("onwer", owner);

        NormalMsg normalMsg = PostRequestUtil.getNormalResponse(toUrl,jsonToAMSystem);

        if (normalMsg.getStatus() == 0){
            logger.info("Error({})",normalMsg.getMessage());
            throw  new Exception(normalMsg.getMessage());
        }

        return normalMsg;
    }
}
