package com.hust.nodecontroller.communication;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.AMSystemInfo;
import com.hust.nodecontroller.infostruct.AnswerStruct.NormalMsg;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName AuthorityModule
 * @date 2020.10.18 12:42
 */

@Component
public class AuthorityModule implements SendInfoToModule{

    /** 权限校验子系统添加权限接口*/
    @Value("${am.save.url}")
    String amSaveUrl;

    /** 权限校验子系统删除权限接口*/
    @Value("${am.delete.url}")
    String amDeleteUrl;

    /** 权限校验子系统更新权限接口*/
    @Value("${am.update.url}")
    String amUpdateUrl;

    /** 权限校验子系统查询权限接口*/
    @Value("${am.query.url}")
    String amQueryUrl;

    private static final Logger logger = LoggerFactory.getLogger(AuthorityModule.class);

    public NormalMsg register(String erpName, String prefix, String key, String authority, String owner) throws Exception{
        return registerAndUpdate(erpName, prefix, key, authority, owner, amSaveUrl);
    }

    public NormalMsg update(String erpName, String prefix, String key, String authority, String owner) throws Exception{
        return registerAndUpdate(erpName, prefix, key, authority, owner, amUpdateUrl);
    }

    public AMSystemInfo query(String client, String prefix, int type){
        long beginTime = System.nanoTime();
        JSONObject jsonToAmSystem = new JSONObject();
        jsonToAmSystem.put("peer_name","peer0");
        jsonToAmSystem.put("erp_name", client);
        jsonToAmSystem.put("identity_prefix", prefix);
        AMSystemInfo amSystemInfo = new AMSystemInfo();
        try {
            amSystemInfo = PostRequestUtil.getAMQueryResponse(amQueryUrl,jsonToAmSystem);

            if (amSystemInfo.getStatus() == 0){
                return amSystemInfo;
            }

            int authority = Integer.parseInt(amSystemInfo.getAuthority(), 2);

            if((authority & type) == 0){
                amSystemInfo.setStatus(0);
                amSystemInfo.setMessage(AuthorityResultEnum.OPERATION_AUTHORITY_VERIFY_ERROR.getMsg());
                return amSystemInfo;
            }
            long endTime = System.nanoTime();
            logger.info("Query Time({}ms)", (endTime-beginTime)/1000000);
            return amSystemInfo;
        }catch (Exception e){
            amSystemInfo.setStatus(0);
            amSystemInfo.setMessage(e.getMessage());
            return amSystemInfo;
        }
    }

    public NormalMsg delete(String erpName, String prefix) throws Exception{
        JSONObject jsonToAmSystem = new JSONObject();
        jsonToAmSystem.put("peer_name", "peer0");
        jsonToAmSystem.put("erp_name", erpName);
        jsonToAmSystem.put("identity_prefix", prefix);

        NormalMsg normalMsg = PostRequestUtil.getNormalResponse(amDeleteUrl,jsonToAmSystem);

        if (normalMsg.getStatus() == 0){
            logger.info("Error({})",normalMsg.getMessage());
            throw  new Exception(normalMsg.getMessage());
        }

        return normalMsg;
    }

    public NormalMsg registerAndUpdate(String erpName, String prefix, String key, String authority, String owner, String toUrl) throws Exception{
        JSONObject jsonToAmSystem = new JSONObject();
        jsonToAmSystem.put("peer_name", "peer0");
        jsonToAmSystem.put("erp_name", erpName);
        jsonToAmSystem.put("identity_prefix", prefix);
        jsonToAmSystem.put("public_key", key);
        jsonToAmSystem.put("authority", authority);
        jsonToAmSystem.put("onwer", owner);

        NormalMsg normalMsg = PostRequestUtil.getNormalResponse(toUrl,jsonToAmSystem);

        if (normalMsg.getStatus() == 0){
            logger.info("Error({})",normalMsg.getMessage());
            throw  new Exception(normalMsg.getMessage());
        }

        return normalMsg;
    }
}
