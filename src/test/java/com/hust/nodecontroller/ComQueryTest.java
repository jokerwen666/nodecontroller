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
        String url = "http://222.180.148.30:8666/Query/086.001.000001/01.02.09.20201102.910009";
        ComQueryInfo comQueryInfo = PostRequestUtil.getComQueryInfo(url);
        comQueryInfo.getStatus();

//        String url = "http://39.107.238.25:8686/api/queryauthoritybynameandprefix";
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("peer_name", "peer1");
//        jsonObject.put("erp_name", "root");
//        jsonObject.put("identity_prefix", "086.001.000001");
//        AMSystemInfo amSystemInfo = PostRequestUtil.getAMQueryResponse(url,jsonObject);
    }
}
