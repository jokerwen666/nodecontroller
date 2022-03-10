package com.hust.nodecontroller.utils;

import com.antherd.smcrypto.sm2.Sm2;
import com.antherd.smcrypto.sm3.Sm3;
import com.hust.nodecontroller.fnlencrypt.RSAUtil;
import com.hust.nodecontroller.fnlencrypt.SM2EncDecUtils;

public class EncDecUtil {
    final static String prikSM = "6adb8ee2ac02a01353a6ecc03cbf88e1beb6cfa9706f74aa9308ff68229c4d46";
    final static String pubkSM = "04e07cd5ce021076ad687c07314b905deb6f6d32be1d4654be2a2c08a6767bce62c7fed615d5ac993610e2d099eceddf6797fd693cd2b244148f33eca6dfb9edbd";

    final static String pubkRSA = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCz1zqQHtHvKczHh58ePiRNgOyiHEx6lZDPlvwBTaHmkNlQyyJ06SIlMU1pmGKxILjT7n06nxG7LlFVUN5MkW/jwF39/+drkHM5B0kh+hPQygFjRq81yxvLwolt+Vq7h+CTU0Z1wkFABcTeQQldZkJlTpyx0c3+jq0o47wIFjq5fwIDAQAB";
    final static String prikRSA = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALPXOpAe0e8pzMeHnx4+JE2A7KIcTHqVkM+W/AFNoeaQ2VDLInTpIiUxTWmYYrEguNPufTqfEbsuUVVQ3kyRb+PAXf3/52uQczkHSSH6E9DKAWNGrzXLG8vCiW35WruH4JNTRnXCQUAFxN5BCV1mQmVOnLHRzf6OrSjjvAgWOrl/AgMBAAECgYAgA0YHdZUFL7mmIvwuE/2+Vh7JVKRAhfM7ILNHQBx7wHkOqro9eWp8mGQhUeDvitWb1C4yizJK0Znkx/pqQtFZuoatUsggocjXFl86FElQwrBp08DvfKfd0bGgy0VTFQVmCtxiqhpAmC7xmXNZXfBD41rl9CKbFfZw05QC5BoQ0QJBAO7LSku97NgFBJQ+vbmVDonuvgnQjVNb7SnwrcpJHEUAGbaVq1a50jz+s6n39TOagASaW6pcY0uwiygYu6xDnkMCQQDAzIGNKFKomTI6djcOyHfQ1ZXqyDQ3guX6nHhzZnNHFF8ZD3fPyyIRSZ3JvPK5iEzJLhB7FRtyWkGcdXgJTWoVAkBfx9zKGqkYUJLwn2XcPWRygPdq2mMFb5bmPqqGu+KB7rNhoBD0nV4tpwALifCpPSxiLEPeRmZxoqN+dsU4KHsfAkAyQt4fK3zpAQ8MGJdf3jkGEzhC/bBHLHPB8pqgEvxIcnIcOWEVpbIa6aMd3Yk1fuftpnmbbLQ8CnWCUUlau3jFAkEAk6bOZIWhTYRwIZcwBdkpyLlbatQFoTTM3i444YutXt3FrFfaWBxge+eYKId+J4dCrt/EmHhSfWKEzHibf6N5Sg==";


    public static String sMEncrypt(String sourceData) {
        return Sm2.doEncrypt(sourceData, pubkSM);
    }

    public static String sMDecrypt(String encryptData) throws Exception {
        String decrypt = null;

        try {
            decrypt = Sm2.doDecrypt(encryptData, prikSM);
        } catch (Exception e) {
            throw new Exception("加解密验证失败！");
        }
        return decrypt;
    }

    public static  String sMHash(String sourceData) {
        return Sm3.sm3(sourceData);
    }

    public static String rsaEncrypt(String sourceData) throws Exception {
        return RSAUtil.encrypt(sourceData, pubkRSA);
    }

    public static String rsaDecrypt(String encryptData) throws Exception {
        String decrypt = null;

        try {
            decrypt = RSAUtil.decrypt(encryptData,prikRSA);
        } catch (Exception e) {
            throw new Exception("加解密验证失败！");
        }
        return decrypt;
    }
}
