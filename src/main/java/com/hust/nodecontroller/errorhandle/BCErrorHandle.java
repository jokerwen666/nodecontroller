package com.hust.nodecontroller.errorhandle;

import com.hust.nodecontroller.communication.BlockchainModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
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

    /**
     * 区块链删除接口url
     */
    private String bcDeleteUrl;

    @Autowired
    public BCErrorHandle(BlockchainModule blockchainModule) {
        this.blockchainModule = blockchainModule;
    }

    @Async
    public void errorHandle(int type, String identity) {
        if (type == 8) {
            blockchainModule.delete(identity,bcDeleteUrl);
        }
    }
}
