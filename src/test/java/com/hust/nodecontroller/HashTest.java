package com.hust.nodecontroller;

import com.hust.nodecontroller.utils.HashUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName HashTest
 * @date 2020.10.20 21:12
 */
public class HashTest {

    @Test
    public void test(){
        String str = "abcdefg";
        String res = Integer.toHexString(HashUtil.apHash(str));
    }
}
