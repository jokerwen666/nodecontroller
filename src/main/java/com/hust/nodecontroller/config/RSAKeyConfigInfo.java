package com.hust.nodecontroller.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @Description
 * 该类用于使用RSA算法模拟加解密过程
 * 公私钥被定义在配置文件中使用
 *
 * @ClassName RSAKeyConfigInfo
 * @author Zhang Bowen
 * @date 2020.09.12 18:48
 */

@Component
@ConfigurationProperties(prefix = "rsa.key")
public class RSAKeyConfigInfo {
    private String rsaPublicKeyStr;
    private String rsaPrivateKeyStr;

    public String getRsaPublicKeyStr() {
        return rsaPublicKeyStr;
    }

    public void setRsaPublicKeyStr(String rsaPublicKeyStr) {
        this.rsaPublicKeyStr = rsaPublicKeyStr;
    }

    public String getRsaPrivateKeyStr() {
        return rsaPrivateKeyStr;
    }

    public void setRsaPrivateKeyStr(String rsaPrivateKeyStr) {
        this.rsaPrivateKeyStr = rsaPrivateKeyStr;
    }

    public PublicKey getPublicKey(String rsaPublicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(rsaPublicKeyStr.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }


    public PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }


    public byte[] encrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");//java默认"RSA"="RSA/ECB/PKCS1Padding"
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }


    public String decrypt(String content, PublicKey publicKey) throws Exception{
        byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        String transferResult = new String(cipher.doFinal(byteContent), StandardCharsets.UTF_8);
        return transferResult;
    }

}
