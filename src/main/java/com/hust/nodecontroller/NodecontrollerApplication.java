package com.hust.nodecontroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync

/*
--ip=39.107.232.47 --server.port=10400 --dPort=10106 --zAddress=39.105.189.17:10001
 */

public class NodecontrollerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NodecontrollerApplication.class, args);
    }

}
