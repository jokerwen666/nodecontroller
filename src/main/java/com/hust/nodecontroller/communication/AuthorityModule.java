package com.hust.nodecontroller.communication;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.enums.ErrorMessageEnum;
import com.hust.nodecontroller.exception.ControlSubSystemException;
import com.hust.nodecontroller.infostruct.answerstruct.AuthorityManagementSystemAnswer;
import com.hust.nodecontroller.infostruct.answerstruct.NormalAnswer;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description 权限校验子系统（区块链）交互类
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

    public NormalAnswer register(String erpName, String prefix, String key, String authority, String owner) throws Exception{
        return registerAndUpdate(erpName, prefix, key, authority, owner, amSaveUrl);
    }

    public NormalAnswer update(String erpName, String prefix, String key, String authority, String owner) throws Exception{
        return registerAndUpdate(erpName, prefix, key, authority, owner, amUpdateUrl);
    }

    public AuthorityManagementSystemAnswer query(String client, String prefix, int type){
        long beginTime = System.nanoTime();
        JSONObject jsonToAmSystem = new JSONObject();
        jsonToAmSystem.put("peer_name","peer0");
        jsonToAmSystem.put("erp_name", client);
        jsonToAmSystem.put("identity_prefix", prefix);
        AuthorityManagementSystemAnswer authorityManagementSystemAnswer = new AuthorityManagementSystemAnswer();
        try {
            authorityManagementSystemAnswer = PostRequestUtil.getAuthorityManagementAnswer(amQueryUrl,jsonToAmSystem);

            if (authorityManagementSystemAnswer.getStatus() == 0){
                return authorityManagementSystemAnswer;
            }

            int authority = Integer.parseInt(authorityManagementSystemAnswer.getAuthority(), 2);

            if((authority & type) == 0){
                authorityManagementSystemAnswer.setStatus(0);
                authorityManagementSystemAnswer.setMessage(ErrorMessageEnum.OPERATION_AUTHORITY_VERIFY_ERROR.getMsg());
                return authorityManagementSystemAnswer;
            }
            long endTime = System.nanoTime();
            logger.info("Query Time({}ms)", (endTime-beginTime)/1000000);
            return authorityManagementSystemAnswer;
        }catch (ControlSubSystemException e){
            authorityManagementSystemAnswer.setStatus(0);
            authorityManagementSystemAnswer.setMessage(e.getMessage());
            return authorityManagementSystemAnswer;
        }
    }

    public NormalAnswer delete(String erpName, String prefix) {
        JSONObject jsonToAmSystem = new JSONObject();
        jsonToAmSystem.put("peer_name", "peer0");
        jsonToAmSystem.put("erp_name", erpName);
        jsonToAmSystem.put("identity_prefix", prefix);
        try {
            return PostRequestUtil.getBlockChainAnswer(amDeleteUrl,jsonToAmSystem);
        } catch (ControlSubSystemException e) {
            NormalAnswer normalAnswer = new NormalAnswer();
            normalAnswer.setStatus(0);
            normalAnswer.setMessage(e.getMessage());
            return normalAnswer;
        }
    }

    public NormalAnswer registerAndUpdate(String erpName, String prefix, String key, String authority, String owner, String toUrl) {
        JSONObject jsonToAmSystem = new JSONObject();
        jsonToAmSystem.put("peer_name", "peer0");
        jsonToAmSystem.put("erp_name", erpName);
        jsonToAmSystem.put("identity_prefix", prefix);
        jsonToAmSystem.put("public_key", key);
        jsonToAmSystem.put("authority", authority);
        jsonToAmSystem.put("onwer", owner);

        try {
            return PostRequestUtil.getBlockChainAnswer(toUrl,jsonToAmSystem);
        } catch (ControlSubSystemException e) {
            NormalAnswer normalAnswer = new NormalAnswer();
            normalAnswer.setStatus(0);
            normalAnswer.setMessage(e.getMessage());
            return normalAnswer;
        }
    }
}
