package com.hust.nodecontroller;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.AMSystemInfo;
import com.hust.nodecontroller.infostruct.ComQueryInfo;
import com.hust.nodecontroller.infostruct.RVSystemInfo;
import com.hust.nodecontroller.utils.HashUtil;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName ComQueryTest
 * @date 2020.11.02 15:12
 */
public class ComQueryTest {
    @Test
    public void test() throws Exception {
        String url = "http://183.67.5.170:8666/Query/086.001.000001/02.03.12.20210407.031003";
        ComQueryInfo comQueryInfo = PostRequestUtil.getComQueryInfo(url);
        String jsonStr = comQueryInfo.getInformation().toString();
        String hash = HashUtil.SM3Hash(jsonStr);
        System.out.println(hash);

//        String url = "http://39.107.238.25:8686/api/queryauthoritybynameandprefix";
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("peer_name", "peer1");
//        jsonObject.put("erp_name", "root");
//        jsonObject.put("identity_prefix", "086.001.000001");
//        AMSystemInfo amSystemInfo = PostRequestUtil.getAMQueryResponse(url,jsonObject);
    }
}
