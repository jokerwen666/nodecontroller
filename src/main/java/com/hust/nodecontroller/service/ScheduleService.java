package com.hust.nodecontroller.service;

import com.alibaba.fastjson.JSONObject;
import com.hust.nodecontroller.infostruct.DhtNodeInfo;
import com.hust.nodecontroller.utils.PostRequestUtil;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@RestController
@RequestMapping(value = "/api")
public class ScheduleService {
    static ZooKeeper zookeeper;
    static Stat stat = new Stat();
    boolean setFlag = false;

    final ApplicationContext applicationContext;
    final String dhtOwnNode;
    final String controllerAddress;
    final String zookeeperAddress;
    private String destination;

    @Autowired
    public ScheduleService(ApplicationContext applicationContext, List<String> serverInfo) {
        this.applicationContext = applicationContext;
        controllerAddress = serverInfo.get(0) + ":" + serverInfo.get(2);
        dhtOwnNode = "http://" + serverInfo.get(0) + ":" + serverInfo.get(1) + "/dht/printList";
        zookeeperAddress = serverInfo.get(3);
    }

    //添加定时任务
    @Scheduled(cron = "0/10 * * * * ?")
    private void configureTasks() {
        AtomicInteger threadNum = (AtomicInteger)applicationContext.getBean("threadNum");
        try {
            DhtNodeInfo node = queryDhtInfo();
            if(node.getStatus()==0) {
                if(setFlag)
                {
                    zookeeper.delete(destination,-1);
                    setFlag=false;
                }
                return;
            }
            if(!setFlag)
            {
                setFlag=true;
                setZook(node.getDomainName());
            }
            stat = zookeeper.setData(destination, (threadNum+"/"+node.toString()).getBytes(), stat.getVersion());
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
            zookeeper = new ZooKeeper(zookeeperAddress, 10000, watcher);
            Thread.sleep(20000);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void setZook(String Domain) throws Exception {
        if (zookeeper.exists("/servers", null) == null) {
            zookeeper.create("/servers", ("0").getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//EPHEMERAL
        }
        if (zookeeper.exists("/servers/"+Domain, null) == null) {
            zookeeper.create("/servers/"+Domain, ("0").getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//EPHEMERAL
        }

        destination="/servers/"+Domain+"/"+controllerAddress;
        if (zookeeper.exists(destination, null) == null) {
            zookeeper.create(destination, ("0").getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//EPHEMERAL
        }
        zookeeper.getData(destination, null, stat);
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
            final ZooKeeper zookeeper = new ZooKeeper(zookeeperAddress, 999999, watcher);
            final byte[] data = zookeeper.getData("/servers/" + controllerAddress, watcher, null);
            value = new String(data);
            zookeeper.close();
        }catch(Exception e){
            e.printStackTrace();
            return "Something goes wrong!";
        }
        return "get value from zookeeper [" + controllerAddress+":"+ value + "]";
    }

    public DhtNodeInfo queryDhtInfo() {
        JSONObject callJson = new JSONObject();
        callJson.put("type", 9);
        return PostRequestUtil.getOwnNodeInfo(dhtOwnNode,callJson);
    }
}