package com.hust.nodecontroller;

import com.hust.nodecontroller.communication.ComInfoModule;
import com.hust.nodecontroller.infostruct.ComQueryInfo;
import com.hust.nodecontroller.utils.EncDecUtil;
import com.hust.nodecontroller.utils.IdTypeJudgeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName HashTest
 * @date 2020.10.20 21:12
 */
public class HashTest {

    @Test
    public void test() {
        String url = "http://39.107.232.47:10422/Query/086.040.000009/1.2.156.40000.27118513";
        ComInfoModule comInfoModule = new ComInfoModule();
        ComQueryInfo comQueryInfo = comInfoModule.query(url);
        String goodsHash = EncDecUtil.sMHash(comQueryInfo.getInformation().toString());
        System.out.println(goodsHash);
    }

    @Test
    public void ecodeTest() {
        String ecodeInfo = IdTypeJudgeUtil.ecodeResolve("1009647007264657137080782", "none");
        System.out.println(ecodeInfo);
    }

}
