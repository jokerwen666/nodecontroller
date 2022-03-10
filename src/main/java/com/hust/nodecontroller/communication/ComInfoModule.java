package com.hust.nodecontroller.communication;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.ComQueryInfo;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName ComInfoModule
 * @date 2020.10.20 18:07
 */

@Component
public class ComInfoModule implements sendInfoToModule{

    private static final Logger logger = LoggerFactory.getLogger(ComInfoModule.class);

    //@Async("comInfoQueryExecutor")
    public ComQueryInfo query(String toUrl){
        ComQueryInfo comQueryInfo = new ComQueryInfo();
        try {
            comQueryInfo = PostRequestUtil.getComQueryInfo(toUrl);
            return comQueryInfo;
        }catch (Exception e){
            comQueryInfo.setStatus(0);
            comQueryInfo.setMessage(e.getMessage());
            return comQueryInfo;
        }
    }
}
