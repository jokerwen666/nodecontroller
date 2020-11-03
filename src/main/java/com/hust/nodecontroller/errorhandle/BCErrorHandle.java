package com.hust.nodecontroller.errorhandle;

import com.hust.nodecontroller.communication.BlockchainModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName BCErrorHandle
 * @date 2020.10.30 20:55
 */

@Component
public class BCErrorHandle {
    private final BlockchainModule blockchainModule;

    //解析结果验证子系统url
    @Value("${bc.register.url}")
    private String bcRegisterUrl;
    @Value("${bc.delete.url}")
    private String bcDeleteUrl;
    @Value("${bc.update.url}")
    private String bcUpdateUrl;
    @Value("${bc.query.url}")
    private String bcQueryUrl;
    @Value("${bc.queryPrefix.url}")
    private String bcPrefixQuery;

    @Autowired
    public BCErrorHandle(BlockchainModule blockchainModule) {
        this.blockchainModule = blockchainModule;
    }

    public void errorHandle(int type, String identity) {
        if (type == 8)
            blockchainModule.delete(identity,bcDeleteUrl);
    }
}
