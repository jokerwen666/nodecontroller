package com.hust.nodecontroller;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.fnlencrypt.SM2EncDecUtils;
import com.hust.nodecontroller.fnlencrypt.SM2SignVerUtils;
import com.hust.nodecontroller.utils.ConvertUtil;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class EncDecTest {

    final static String prik = "4cf170068e9c47ebdb521fb9fc62c4a55a5773fb9da33b0acf8129e28d09d205";
    final static String pubk = "04aabda53043e8dcb86d42f690b61a4db869821dadf9f851ec3c5c43d0c8f95a6677fdba984afc3bb010a8436b1d17cefc2011a34e01e9e801124d29ffa928d803";
    final static JSONObject json = new JSONObject();
    final static byte[] sourceData;
    static {
        json.put("status", 0);
        json.put("message", "086.001.000001/02.03.11.20210407.021010");
        String jsonString = json.toString();
        String id = "086.001.000001/02.03.11.20210407.021010";
        sourceData = id.getBytes();
    }

    @Test
    public void signTest() throws Exception {
        //签名测试
        String sign = SM2SignVerUtils.Sign2SM2(ConvertUtil.hexStringToBytes(prik), sourceData, "zhangbowen");
        boolean verify = SM2SignVerUtils.VerifySignSM2(ConvertUtil.hexStringToBytes(pubk), sourceData, ConvertUtil.hexStringToBytes(sign), "zhangbowen");
        System.out.println(verify);
    }

    @Test
    public void encryptTest() throws Exception {
        //加解密测试
        String encrypt = SM2EncDecUtils.encrypt(ConvertUtil.hexToByte(pubk), sourceData);
        System.out.println(encrypt);
        String decrypt = null;
        try {
            decrypt = new String(SM2EncDecUtils.decrypt(ConvertUtil.hexToByte(prik), ConvertUtil.hexToByte(encrypt)));
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
}
