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
    static String Destination;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    String IPAndPort;
    DhtNodeInfo node;

    //添加定时任务
    @Scheduled(cron = "0/10 * * * * ?")
    private void configureTasks() {
        if(DhtNodeInfo.getFlag())
        {
            AtomicInteger Threadnum=(AtomicInteger)applicationContext.getBean("ThreadNum");
            // DHTnodeinfo node=(DHTnodeinfo)applicationContext.getBean("DHTnodeinfo");
            String result;
            try {
                result=Threadnum + "/"+node.toString();
                System.out.println(result);
                stat = zookeeper.setData("/servers/" + Destination, (result).getBytes(), stat.getVersion());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        }
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
            zookeeper = new ZooKeeper("127.0.0.1:2181", 10000, watcher);
            Thread.sleep(20000);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void Setzook(String Domain,String IPandPort) throws Exception {

        if (zookeeper.exists("/servers", null) == null) {
            zookeeper.create("/servers", ("0").getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//EPHEMERAL
        }
        if (zookeeper.exists("/servers/" + Domain, null) == null) {
            zookeeper.create("/servers/" + Domain, ("0").getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//EPHEMERAL
        }

        Destination=Domain+"/"+IPandPort;
        if (zookeeper.exists("/servers/" + Destination, null) == null) {
            zookeeper.create("/servers/" + Destination, ("0").getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//EPHEMERAL
        }

        zookeeper.getData("/servers/" + Destination, null, stat);
    }


    @PostMapping(value = "/node")
    @ResponseBody
    public NormalMsg Param(@RequestBody DhtNodeInfo node) {
        NormalMsg backHtml = new NormalMsg();
        try {
            this.node = node;
            ScheduleService.Setzook("domain" + node.getDomainName(), IPAndPort);
            node.setFlag(true);
            backHtml.setStatus(1);
            backHtml.setMessage("Success!");
        }catch (Exception e)
        {
            backHtml.setStatus(0);
            backHtml.setMessage(e.getMessage());
        }
        return backHtml;
    }


    @RequestMapping(value = "/exit")
    public void Param() {
        NormalMsg backHtml = new NormalMsg();
        backHtml.setStatus(1);
        backHtml.setMessage("Success!");
        System.exit(0);
    }
}