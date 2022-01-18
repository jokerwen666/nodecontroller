package com.hust.nodecontroller.communication;

import com.hust.nodecontroller.infostruct.ComQueryInfo;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName ComInfoModule
 * @date 2020.10.20 18:07
 */

@Component
public class ComInfoModule implements SendInfoToModule{

    private static final Logger logger = LoggerFactory.getLogger(ComInfoModule.class);

    public ComQueryInfo query(String toUrl){
        long beginTime = System.nanoTime();
        ComQueryInfo comQueryInfo = new ComQueryInfo();
        try {
            comQueryInfo = PostRequestUtil.getComQueryInfo(toUrl);
            long endTime = System.nanoTime();
            logger.info("Query Time({}ms)", (endTime-beginTime)/1000000);
            return comQueryInfo;
        }catch (Exception e){
            comQueryInfo.setStatus(0);
            comQueryInfo.setMessage(e.getMessage());
            return comQueryInfo;
        }
    }
}
