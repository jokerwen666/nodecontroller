package com.hust.nodecontroller.fnlencrypt.hashutils;

import com.hust.nodecontroller.utils.ConvertUtil;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.springframework.stereotype.Component;

@Component
public class SM3Hash implements HashUtils {
    @Override
    public String toHashString(String data) {
        SM3Digest sm3Digest = new SM3Digest();
        byte[] dataBytes = data.getBytes();
        sm3Digest.update(dataBytes, 0, dataBytes.length);
        byte [] md = new byte[32];
        sm3Digest.doFinal(md, 0);
        return ConvertUtil.getHexString(md);
    }
}
