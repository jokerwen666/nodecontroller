package com.hust.nodecontroller.utils;

import org.bouncycastle.crypto.digests.SM3Digest;

/**
 * 该工具类永用于定义若干种hash算法
 * 用于对url进行hash运算
 *
 * @author Zhang Bowen
 * @Description
 * @ClassName HashUtil
 * @date 2020.09.14 12:19
 */
public class HashUtil {
    /**
     * SM3杂凑函数
     *
     * @param data 字符串
     * @return hash结果
     */
    public static String SM3Hash(String data) {
        SM3Digest sm3Digest = new SM3Digest();
        byte[] dataBytes = data.getBytes();
        sm3Digest.update(dataBytes, 0, dataBytes.length);
        byte [] md = new byte[32];
        sm3Digest.doFinal(md, 0);
        return ConvertUtil.getHexString(md);
    }

    /**
     * AP算法
     *
     * @param str 字符串
     * @return hash值
     */

    public static int apHash(String str) {
        int hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash ^= ((i & 1) == 0) ? ((hash << 7) ^ str.charAt(i) ^ (hash >> 3)) : (~((hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
        }
        return hash;
    }
}

