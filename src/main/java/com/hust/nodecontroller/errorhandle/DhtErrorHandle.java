package com.hust.nodecontroller.errorhandle;

import com.hust.nodecontroller.communication.DhtModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Zhang Bowen
 * @Description 用来恢复操作
 *
 * 需要dht和区块链在更新、删除的时候给出对应更新、删除前的信息
 *
 * @ClassName DhtErrorHandle
 * @date 2020.10.30 20:54
 */

@Component
public class DhtErrorHandle {

    @Value("${dht.register.url}")
    private String dhtRegisterUrl;
    @Value("${dht.delete.url}")
    private String dhtDeleteUrl;
    @Value("${dht.update.url}")
    private String dhtUpdateUrl;
    @Value("${dht.query.url}")
    private String dhtQueryUrl;
    @Value("${dht.allNode.url}")
    private String dhtAllNode;

    private final DhtModule dhtModule;

    @Autowired
    public DhtErrorHandle(DhtModule dhtModule) {
        this.dhtModule = dhtModule;
    }

    @Async
    public void errorHandle(int type, String identity, String prefix) {
        if (type == 8) {
            dhtModule.delete(identity,prefix,dhtDeleteUrl,4);
        }
    }
}
