package com.hust.nodecontroller.service;

import com.hust.nodecontroller.controller.NodeController;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName ScheduleService
 * @date 2020.10.16 13:30
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@RestController
public class ScheduleService {
    static ZooKeeper zookeeper;
    static Stat stat = new Stat();
    private final String destination;
    private final AtomicInteger threadNum;


    //添加定时任务
    @Scheduled(cron = "0/8 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        try {
            stat = zookeeper.setData("/servers/" + destination, (threadNum + "").getBytes(), stat.getVersion());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
       // System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }

    @Autowired
    public ScheduleService(AtomicInteger threadNum)
    {
        this.threadNum = threadNum;
        destination = "112.125.88.24:10400";
//        Scanner scan = new Scanner(System.in);
//        System.out.println("Please input Server's IP and port in form:  IP:port");
//        destination = scan.next();
//        System.out.println("Server works on " + destination);
    }

    @PostConstruct
    public void Init()
    {
        //   nodeController=(NodeController)applicationContext.getBean("NodeController");
        try {
            Watcher watcher = new Watcher(){
                public void process(WatchedEvent event) {
                    System.out.println("receive event："+event);
                }
            };
            String value = null;
            zookeeper = new ZooKeeper("112.125.88.24:10001", 10000, watcher);
            Thread.sleep(10000);

            if (zookeeper.exists("/servers", null) == null) {
                zookeeper.create("/servers", (threadNum+"").getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//EPHEMERAL
            }
            if (zookeeper.exists("/servers/"+destination, null) == null) {
                zookeeper.create("/servers/"+destination, (threadNum+"").getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//EPHEMERAL
            }
            zookeeper.getData("/servers/"+destination,true,stat);
            System.out.println("Data version:"+stat.getVersion());

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "/zk")
    public String zk() {
        threadNum.incrementAndGet();
        return "123456";
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
            final ZooKeeper zookeeper = new ZooKeeper("112.125.88.24:10001", 999999, watcher);
            final byte[] data = zookeeper.getData("/servers/"+destination, watcher, null);
            value = new String(data);
            zookeeper.close();
        }catch(Exception e){
            e.printStackTrace();
            return "Something goes wrong!";
        }

        return "get value from zookeeper [" + value + "]";
    }
}