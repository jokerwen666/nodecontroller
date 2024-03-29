package com.hust.nodecontroller.fnlencrypt;


import com.hust.nodecontroller.utils.ConvertUtil;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;
import java.math.BigInteger;

public class SM2EncDecUtils {
    //生成随机秘钥对
    public static SM2KeyVO generateKeyPair(){
        SM2 sm2 = SM2.Instance();
        AsymmetricCipherKeyPair key = null;
        while (true){
            key=sm2.ecc_key_pair_generator.generateKeyPair();
            if(((ECPrivateKeyParameters) key.getPrivate()).getD().toByteArray().length==32){
                break;
            }
        }
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();
        SM2KeyVO sm2KeyVO = new SM2KeyVO();
        sm2KeyVO.setPublicKey(publicKey);
        sm2KeyVO.setPrivateKey(privateKey);
        System.out.println("公钥: " + ConvertUtil.byteToHex(publicKey.getEncoded()));
        System.out.println("私钥: " + ConvertUtil.byteToHex(privateKey.toByteArray()));
        return sm2KeyVO;
    }

    //数据加密
    public static String encrypt(byte[] publicKey, byte[] data) throws IOException
    {
        if (publicKey == null || publicKey.length == 0)
        {
            return null;
        }

        if (data == null || data.length == 0)
        {
            return null;
        }

        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);

        Cipher cipher = new Cipher();
        SM2 sm2 = new SM2();
        ECPoint userKey = sm2.ecc_curve.decodePoint(publicKey);

        ECPoint c1 = cipher.Init_enc(sm2, userKey);
        cipher.Encrypt(source);
        byte[] c3 = new byte[32];
        cipher.Dofinal(c3);

//      System.out.println("C1 " + Util.byteToHex(c1.getEncoded()));
//      System.out.println("C2 " + Util.byteToHex(source));
//      System.out.println("C3 " + Util.byteToHex(c3));
        //C1 C2 C3拼装成加密字串
        // C1 | C2 | C3
        //return Util.byteToHex(c1.getEncoded()) + Util.byteToHex(source) + Util.byteToHex(c3);
        // C1 | C3 | C2
        return ConvertUtil.byteToHex(c1.getEncoded()) + ConvertUtil.byteToHex(c3) + ConvertUtil.byteToHex(source);
    }

    //数据解密
    public static byte[] decrypt(byte[] privateKey, byte[] encryptedData) throws IOException
    {
        if (privateKey == null || privateKey.length == 0)
        {
            return null;
        }

        if (encryptedData == null || encryptedData.length == 0)
        {
            return null;
        }
        //加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2
        String data = ConvertUtil.byteToHex(encryptedData);
        /***分解加密字串 C1 | C2 | C3
         * （C1 = C1标志位2位 + C1实体部分128位 = 130）
         * （C3 = C3实体部分64位  = 64）
         * （C2 = encryptedData.length * 2 - C1长度  - C2长度）

         byte[] c1Bytes = Util.hexToByte(data.substring(0,130));
         int c2Len = encryptedData.length - 97;
         byte[] c2 = Util.hexToByte(data.substring(130,130 + 2 * c2Len));
         byte[] c3 = Util.hexToByte(data.substring(130 + 2 * c2Len,194 + 2 * c2Len));
         */
        /***分解加密字串 C1 | C3 | C2
         * （C1 = C1标志位2位 + C1实体部分128位 = 130）
         * （C3 = C3实体部分64位  = 64）
         * （C2 = encryptedData.length * 2 - C1长度  - C2长度）
         */
        byte[] c1Bytes = ConvertUtil.hexToByte(data.substring(0,130));
        int c2Len = encryptedData.length - 97;
        byte[] c3 = ConvertUtil.hexToByte(data.substring(130,130 + 64));
        byte[] c2 = ConvertUtil.hexToByte(data.substring(194,194 + 2 * c2Len));

        SM2 sm2 = SM2.Instance();
        BigInteger userD = new BigInteger(1, privateKey);

        //通过C1实体字节来生成ECPoint
        ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
        Cipher cipher = new Cipher();
        cipher.Init_dec(userD, c1);
        cipher.Decrypt(c2);
        cipher.Dofinal(c3);

        //返回解密结果
        return c2;
    }

/*    public static BigInteger[] Sm2Sign(byte[] md, AsymmetricCipherKeyPair keypair)
    {
        SM3Digest sm3 = new SM3Digest();

        ECPublicKeyParameters ecpub = (ECPublicKeyParameters)keypair.getPublic();

        byte[] z = SM2CryptoServiceProvider.Sm2GetZ(Encoding.Default.GetBytes(SM2CryptoServiceProvider.userId), ecpub.getQ());
        sm3.update(z, 0, z.length);

        byte[] p = md;
        sm3.update(p, 0, p.length);

        byte[] hashData = new byte[32];
        sm3.doFinal(hashData, 0);

        // e
        BigInteger e = new BigInteger(1, hashData);
        // k
        BigInteger k = null;
        ECPoint kp = null;
        BigInteger r = null;
        BigInteger s = null;
        BigInteger userD = null;

        do
        {
            do
            {

                ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters)keypair.getPrivate();
                k = ecpriv.getD();
                kp = ecpub.getQ();

                userD = ecpriv.getD();

                // r
                r = e.add(kp.getX().toBigInteger());
                r = r.mod(ecc_n);
            }
            while (r.equals(BigInteger.ZERO) || r.add(k).equals(ecc_n));

            // (1 + dA)~-1
            BigInteger da_1 = userD.add(BigInteger.ONE);
            da_1 = da_1.modInverse(ecc_n);
            // s
            s = r.multiply(userD);
            s = k.subtract(s).mod(ecc_n);
            s = da_1.multiply(s).mod(ecc_n);
        }
        while (s.equals(BigInteger.ZERO));

        byte[] btRS = new byte[64];
        byte[] btR = r.toByteArray();
        byte[] btS = s.toByteArray();
        Array.Copy(btR, btR.length - 32, btRS, 0, 32);
        Array.Copy(btS, btS.length - 32, btRS, 32, 32);

        return new BigInteger[] { r, s };
    }*/
}
