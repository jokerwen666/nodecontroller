package com.hust.nodecontroller.infostruct;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.exception.FormatException;
import com.hust.nodecontroller.enums.FormatResultEnum;
import org.springframework.stereotype.Component;

/**
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
 * @author Zhang Bowen
 * @ClassName InfoFromClient
 * @date 2020.09.12 16:51
 */

@Component
public class InfoFromClient {

    private String identification;
    private String orgPrefix;
    private JSONObject data;
    private String client;
    private Boolean crossDomain_flag = false;

    private String hashType = "sm2";
    private String signData;
    private String encryptData;

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

    public String getSignData() { return signData; }

    public void setSignData(String signData) { this.signData = signData; }

    public String getEncryptData() { return encryptData; }

    public void setEncryptData(String encryptData) { this.encryptData = encryptData; }

    public void setCrossDomain_flag(Boolean crossDomain_flag) {
        this.crossDomain_flag = crossDomain_flag;
    }

    /**
     * @Description : 获得标识前缀
     * @author : Zhang Boweno'l'p
     * @date : 2020.09.15 10:55

     * @return : java.lang.String
     */
    public static String getPrefix(String identification) throws FormatException {
        String[] idList = identification.split("/");
        if (idList.length != 2) {
            throw new FormatException(FormatResultEnum.IDENTIFICATION_PREFIX_SPLIT_ERROR);
        }

        else {
            String prefix = idList[0];
            String suffix = idList[1];
            return prefix;
        }
    }

    public static String getDomainPrefix(String identification) throws FormatException {
        String prefix = getPrefix(identification);
        int pos = prefix.lastIndexOf(".");
        if (pos == -1) {
            throw new FormatException(FormatResultEnum.INDETIFICATION_DOMAIN_PREFIX_ERROR);
        }

        return prefix.substring(0,pos);
    }

    public String getHashType() {
        return hashType;
    }

    public void setHashType(String hashType) {
        this.hashType = hashType;
    }
}
