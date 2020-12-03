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
        String data = "http://222.180.148.30:8666/Query/086.001.000001/01.02.06.20201119.600057";

        String data_ = " http://222.180.148.30:8666/Query/086.001.000001/01.02.06.20201119.600057";

        String dataHash = Integer.toHexString(HashUtil.apHash(data));

        String dataHash_ = Integer.toHexString(HashUtil.apHash(data_));

    }
}
