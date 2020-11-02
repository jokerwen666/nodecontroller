package com.hust.nodecontroller.service;

import com.hust.nodecontroller.controller.NodeController;
import com.hust.nodecontroller.infostruct.DhtNodeInfo;
import com.hust.nodecontroller.infostruct.DhtNodeInfo;
import com.hust.nodecontroller.infostruct.NormalMsg;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@RestController
@RequestMapping(value = "/api")
public class ScheduleService {
    static ZooKeeper zookeeper;
    static Stat stat=new Stat();

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    String IPAndPort;
    DhtNodeInfo node;

    //添加定时任务
    @Scheduled(cron = "0/1 * * * * ?")
    private void configureTasks() {
        AtomicInteger Threadnum=(AtomicInteger)applicationContext.getBean("threadNum");
        // DHTnodeinfo node=(DHTnodeinfo)applicationContext.getBean("DHTnodeinfo");
        //String result;
        try {
            stat = zookeeper.setData("/servers/" + IPAndPort, (Threadnum+"").getBytes(), stat.getVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
       //System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }

    @PostConstruct
    public void Init()
    {
        try {
            Watcher watcher= new Watcher(){
                public void process(WatchedEvent event) {
                    System.out.println("receive event："+event);
                }
            };
            String value = null;
            zookeeper = new ZooKeeper("39.105.189.17:10001", 10000, watcher);
            Thread.sleep(20000);
            Setzook(IPAndPort);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void Setzook(String IPandPort) throws Exception {

        if (zookeeper.exists("/servers", null) == null) {
            zookeeper.create("/servers", ("0").getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//EPHEMERAL
        }
        if (zookeeper.exists("/servers/" + IPandPort, null) == null) {
            zookeeper.create("/servers/" + IPandPort, ("0").getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//EPHEMERAL
        }
        zookeeper.getData("/servers/" + IPandPort, null, stat);
    }

    @RequestMapping(value = "/zkget")
    public String zkget() {
        Watcher watcher = new Watcher(){
            public void process(WatchedEvent event) {
                System.out.println("receive event："+event);
            }
        };

        String value = null;
        try {
            final ZooKeeper zookeeper = new ZooKeeper("39.105.189.17:10001", 999999, watcher);
            final byte[] data = zookeeper.getData("/servers/"+IPAndPort, watcher, null);
            value = new String(data);
            zookeeper.close();
        }catch(Exception e){
            e.printStackTrace();
            return "Something goes wrong!";
        }
        return "get value from zookeeper [" + IPAndPort+":"+ value + "]";
    }

}