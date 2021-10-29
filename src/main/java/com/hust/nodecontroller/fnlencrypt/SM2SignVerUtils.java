package com.hust.nodecontroller.fnlencrypt;


import com.hust.nodecontroller.utils.ConvertUtil;
import org.bouncycastle.asn1.*;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.math.ec.ECPoint;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.Enumeration;

/**
 * 国密算法的签名、验签
 */
public class SM2SignVerUtils {
    /**
     * 默认USERID
     */
    //public static String USER_ID = "1234567812345678";

    /**
     * 私钥签名
     * 使用SM3进行对明文数据计算一个摘要值
     * @param privatekey 私钥
     * @param sourceData 明文数据
     * @return 签名后的值
     * @throws Exception
     */
    public static String Sign2SM2(byte[] privatekey,byte[] sourceData,String userID) throws Exception{
        //SM2SignVO sm2SignVO = new SM2SignVO();
        //sm2SignVO.setSm2_type("sign");

        //生成SM2实例
        SM2 sm2 = SM2.Instance();
        BigInteger userD = new  BigInteger(privatekey);
        //System.out.println("userD:"+userD.toString(16));
        //sm2SignVO.setSm2_userd(userD.toString(16));

        //根据私钥dA，生成公钥PA(xA,yA)
        ECPoint userKey = sm2.ecc_point_g.multiply(userD);
        //System.out.println("椭圆曲线点X: "+ userKey.getXCoord().toBigInteger().toString(16));
        //System.out.println("椭圆曲线点Y: "+ userKey.getYCoord().toBigInteger().toString(16));


        //根据userid和公钥生成杂凑值ZA
        SM3Digest sm3Digest = new SM3Digest();
        byte [] z = sm2.sm2GetZ(userID.getBytes(), userKey);
        //System.out.println("SM3摘要Z: " + Util.getHexString(z));
        //System.out.println("被加密数据的16进制: " + Util.getHexString(sourceData));
        //sm2SignVO.setSm3_z(Util.getHexString(z));
        //sm2SignVO.setSign_express(Util.getHexString(sourceData));

        //将杂凑值ZA与原数据拼凑并使用sm3计算Hash值
        sm3Digest.update(z, 0, z.length);
        sm3Digest.update(sourceData,0,sourceData.length);
        byte [] md = new byte[32];
        sm3Digest.doFinal(md, 0);
        //System.out.println("SM3摘要值: " + Util.getHexString(md));
        //sm2SignVO.setSm3_digest(Util.getHexString(md));

        //根据上一步计算得到的Hash值和公私钥计算得到签名
        SM2Result sm2Result = new SM2Result();
        sm2.sm2Sign(md, userD, userKey, sm2Result);
        //System.out.println("r: " + sm2Result.r.toString(16));
        //System.out.println("s: " + sm2Result.s.toString(16));
        //sm2SignVO.setSign_r(sm2Result.r.toString(16));
        //sm2SignVO.setSign_s(sm2Result.s.toString(16));


        //
        ASN1Integer d_r = new ASN1Integer(sm2Result.r);
        ASN1Integer d_s = new ASN1Integer(sm2Result.s);
        ASN1EncodableVector v2 = new ASN1EncodableVector();
        v2.add(d_r);
        v2.add(d_s);
        DERSequence sign = new DERSequence(v2);
        String result = ConvertUtil.byteToHex(sign.getEncoded());
        //sm2SignVO.setSm2_sign(result);
        //return sm2SignVO;
        return result;
    }
    /**
     * 验证签名
     * @param publicKey 公钥信息
     * @param sourceData 密文信息
     * @param signData 签名信息
     * @return 验签的对象 包含了相关参数和验签结果
     */
    @SuppressWarnings("unchecked")
    public static boolean VerifySignSM2(byte[] publicKey,byte[] sourceData,byte[] signData, String userID){
        try {
            byte[] formatedPubKey;
            //SM2SignVO verifyVo = new SM2SignVO();
            //verifyVo.setSm2_type("verify");
            if (publicKey.length == 64) {
                // 添加一字节标识，用于ECPoint解析
                formatedPubKey = new byte[65];
                formatedPubKey[0] = 0x04;
                System.arraycopy(publicKey, 0, formatedPubKey, 1, publicKey.length);
            } else{
                formatedPubKey = publicKey;
            }
            SM2 sm2 = SM2.Instance();
            ECPoint userKey = sm2.ecc_curve.decodePoint(formatedPubKey);

            SM3Digest sm3Digest = new SM3Digest();
            byte [] z = sm2.sm2GetZ(userID.getBytes(), userKey);
            //System.out.println("SM3摘要Z: " + Util.getHexString(z));
            //verifyVo.setSm3_z(Util.getHexString(z));

            sm3Digest.update(z,0,z.length);
            sm3Digest.update(sourceData,0,sourceData.length);
            byte [] md = new byte[32];
            sm3Digest.doFinal(md, 0);
            //System.out.println("SM3摘要值: " + Util.getHexString(md));
            //verifyVo.setSm3_digest(Util.getHexString(md));

            ByteArrayInputStream bis = new ByteArrayInputStream(signData);
            ASN1InputStream dis = new ASN1InputStream(bis);
            SM2Result sm2Result = null;
            ASN1Primitive derObj = dis.readObject();
            Enumeration<ASN1Integer> e = ((ASN1Sequence)derObj).getObjects();
            BigInteger r = ((ASN1Integer) e.nextElement()).getValue();
            BigInteger s = ((ASN1Integer) e.nextElement()).getValue();
            sm2Result = new SM2Result();
            sm2Result.r = r;
            sm2Result.s = s;
            //System.out.println("vr: " + sm2Result.r.toString(16));
            //System.out.println("vs: " + sm2Result.s.toString(16));
            //verifyVo.setVerify_r(sm2Result.r.toString(16));
            //verifyVo.setVerify_s(sm2Result.s.toString(16));
            sm2.sm2Verify(md, userKey, sm2Result.r, sm2Result.s, sm2Result);
            boolean verifyFlag = sm2Result.r.equals(sm2Result.R);
            //verifyVo.setVerify(verifyFlag);
            //return  verifyVo;
            return verifyFlag;
        } catch (IllegalArgumentException e) {
            //return null;
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            //return null;
            return false;
        }
    }
}

