package com.hust.nodecontroller;

import com.alibaba.fastjson.JSONObject;
import com.antherd.smcrypto.sm2.Keypair;
import com.antherd.smcrypto.sm2.Sm2;
import com.hust.nodecontroller.fnlencrypt.SM2EncDecUtils;
import com.hust.nodecontroller.fnlencrypt.SM2SignVerUtils;
import com.hust.nodecontroller.utils.ConvertUtil;
import com.hust.nodecontroller.utils.EncDecUtil;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class EncDecTest {

    final static String prikSM = "6adb8ee2ac02a01353a6ecc03cbf88e1beb6cfa9706f74aa9308ff68229c4d46";
    final static String pubkSM = "04e07cd5ce021076ad687c07314b905deb6f6d32be1d4654be2a2c08a6767bce62c7fed615d5ac993610e2d099eceddf6797fd693cd2b244148f33eca6dfb9edbd";
    final static JSONObject json = new JSONObject();
    final static byte[] sourceData ="".getBytes();
    static {
        json.put("status", 0);
        json.put("message", "086.001.000001/02.03.11.20210407.021010");
        String jsonString = json.toString();
    }

    @Test
    public void signTest() throws Exception {
        //签名测试
        String sign = SM2SignVerUtils.Sign2SM2(ConvertUtil.hexStringToBytes(prikSM), sourceData, "zhangbowen");
        boolean verify = SM2SignVerUtils.VerifySignSM2(ConvertUtil.hexStringToBytes(pubkSM), sourceData, ConvertUtil.hexStringToBytes(sign), "zhangbowen");
        System.out.println(verify);
    }

    @Test
    public void encryptTest() throws Exception {
        //加解密测试
        String str = "086.001.000001/02.03.09.20210407.910006";
        String encrypt0 = EncDecUtil.sMEncrypt(str);
        String encrypt = SM2EncDecUtils.encrypt(ConvertUtil.hexToByte(pubkSM), str.getBytes());
        System.out.println(encrypt);
        String decrypt = null;
        try {
            decrypt = new String(SM2EncDecUtils.decrypt(ConvertUtil.hexToByte(prikSM), ConvertUtil.hexToByte(encrypt0)));
        } catch (Exception e) {
            System.out.println("验证失败");
        }
        System.out.println(decrypt);
    }

    @Test
    public void hashTest() throws Exception {
        String url = "hust/ncc/zbw";
        SM3Digest sm3Digest = new SM3Digest();
        sm3Digest.update(url.getBytes(), 0, url.getBytes().length);
        byte [] md = new byte[32];
        sm3Digest.doFinal(md, 0);
        String hashUrl = ConvertUtil.getHexString(md);
        System.out.println(hashUrl);

    }


    @Test
    public void generateKey() throws Exception {
        Keypair keypair = Sm2.generateKeyPairHex();
        String privateKey = keypair.getPrivateKey();
        String publicKey = keypair.getPublicKey();
        System.out.println(privateKey);
        System.out.println(publicKey);
    }

    @Test
    public void sm2EncDec() throws Exception {
        String str = "a67f7d147cdd4611a205cbfdf634146dd6664ed73b53056d1ecc5e3f5b06e6cacf2c9f4b4413eec170ab79fe4fe4aeee858de653612a63700ff67fec17add05e2940c7311e7b86bb183c735c9e7a881bfeb2390cac3f1874a980fe31221355f329afca90b936ad5c2523040dcceb741a770d7b261e63d6fa135cc463";
        String decryptData = EncDecUtil.sMDecrypt(str);
        System.out.println(decryptData);
    }
}
