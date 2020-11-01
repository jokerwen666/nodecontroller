package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.exception.FormatException;
import com.hust.nodecontroller.enums.FormatResultEnum;
import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description
 * 该类用于存储从客户端获得的相关信息；
 * 控制模块会从客户端中首先获得：标识 + <标识，操作类型、映射数据>的密文；
 * 然后根据标识提取企业前缀，并将其发送给区块链；
 * 因为模拟程序从前端接收到的json数据默认不加密；
 * 所以需要手动加密为密文来模拟加解密过程；
 *
 * identification字段表示标识；
 * Context字段表示明文
 * identificationCipher字段表示密文
 * privateKeyStr表示加密的密文
 *
 * @ClassName InfoFromClient
 * @date 2020.09.12 16:51
 */

@Component
public class InfoFromClient {

    // ContextCipher中包含的是标识、操作类型、url、产品信息摘要、对标识操作的权限
    // 中间用“:”分割，例如明文为：
    // edu.COMP1.xx/abc:type:url:goodsinfohash:authority

    private String identification;
    private String orgPrefix;
    private JSONObject data;
    private String client;
    private Boolean crossDomain_flag = false;

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getOrgPrefix() {
        return orgPrefix;
    }

    public void setOrgPrefix(String orgPrefix) {
        this.orgPrefix = orgPrefix;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Boolean getCrossDomain_flag() {
        return crossDomain_flag;
    }

    public void setCrossDomain_flag(Boolean crossDomain_flag) {
        this.crossDomain_flag = crossDomain_flag;
    }

    /**
     * @Description : 获得标识前缀
     * @author : Zhang Bowen
     * @date : 2020.09.15 10:55

     * @return : java.lang.String
     */
    public String getPrefix() throws FormatException {
        String[] idList = this.identification.split("/");
        if (idList.length != 2) {
            throw new FormatException(FormatResultEnum.IDENTIFICATION_PREFIX_SPLIT_ERROR);
        }

        else {
            String prefix = idList[0];
            String suffix = idList[1];
            return prefix;
        }
    }


}
