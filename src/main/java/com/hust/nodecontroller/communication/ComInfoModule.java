package com.hust.nodecontroller.communication;

import com.hust.nodecontroller.infostruct.answerstruct.QueryGoodsInfoAnswer;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description 公司产品信息查询子系统交互类
 * @ClassName BlockchainModule
 * @date 2020.10.18 12:28
 */

@Component
public class ComInfoModule implements SendInfoToModule{

    private static final Logger logger = LoggerFactory.getLogger(ComInfoModule.class);

    public QueryGoodsInfoAnswer query(String toUrl){
        long beginTime = System.nanoTime();
        QueryGoodsInfoAnswer queryGoodsInfoAnswer = new QueryGoodsInfoAnswer();
        try {
            queryGoodsInfoAnswer = PostRequestUtil.queryGoodsInfoAnswer(toUrl);
            long endTime = System.nanoTime();
            logger.info("Query Time({}ms)", (endTime-beginTime)/1000000);
            return queryGoodsInfoAnswer;
        }catch (Exception e){
            queryGoodsInfoAnswer.setStatus(0);
            queryGoodsInfoAnswer.setMessage(e.getMessage());
            return queryGoodsInfoAnswer;
        }
    }
}
