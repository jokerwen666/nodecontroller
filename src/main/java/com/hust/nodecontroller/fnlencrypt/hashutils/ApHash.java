package com.hust.nodecontroller.fnlencrypt.hashutils;

import com.hust.nodecontroller.utils.ConvertUtil;
import com.hust.nodecontroller.utils.HashUtil;
import org.springframework.stereotype.Component;

@Component
public class ApHash implements HashUtils {
    @Override
    public String toHashString(String data) {
        int hash = 0;

        for (int i = 0; i < data.length(); i++) {
            hash ^= ((i & 1) == 0) ? ((hash << 7) ^ data.charAt(i) ^ (hash >> 3)) : (~((hash << 11) ^ data.charAt(i) ^ (hash >> 5)));
        }
        return Integer.toHexString(hash);
    }
}
