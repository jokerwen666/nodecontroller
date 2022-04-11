package com.hust.nodecontroller.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author Zhang Bowen
 * @Description
 * 该类为线程池的配置类
 * 对线程池中的相关参数进行了定义
 *
 * @ClassName ThreadPoolConfig
 * @date 2020.09.19 19:08
 */

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolConfig.class);

    /**
     * @Description : 定义企业操作（增删改）的线程池
     * @author : Zhang Bowen
     * @date : 2020.09.19 19:34
     * @return : java.util.concurrent.Executor
     */
    @Bean("enterpriseHandleExecutor")
    public Executor enterpriseHandleExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //配置核心线程池数
        taskExecutor.setCorePoolSize(10);
        //配置最大线程数
        taskExecutor.setMaxPoolSize(50);
        //配置队列大小
        taskExecutor.setQueueCapacity(200);
        //配置空闲线程存活时间
        taskExecutor.setKeepAliveSeconds(60);
        //配置线程池中的线程名称前缀
        taskExecutor.setThreadNamePrefix("enterprise-handle-");
        //配置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        //用于保证异步执行时Spring容器中其他资源的安全
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 配置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁
        // 以确保应用最后能够被关闭，而不是阻塞住
        taskExecutor.setAwaitTerminationSeconds(60);
        return taskExecutor;
    }

    /**
     * @Description : 定义用户操作（查询）的线程池
     * @author : Zhang Bowen
     * @date : 2021.11.24 13:55
     * @return : java.util.concurrent.Executor
     */

    @Bean("queryHandleExecutor")
    public Executor queryHandleExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //配置核心线程池数
        taskExecutor.setCorePoolSize(10);
        //配置最大线程数
        taskExecutor.setMaxPoolSize(50);
        //配置队列大小
        taskExecutor.setQueueCapacity(200);
        //配置空闲线程存活时间
        taskExecutor.setKeepAliveSeconds(60);
        //配置线程池中的线程名称前缀
        taskExecutor.setThreadNamePrefix("comInfo-query-");
        //配置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        //用于保证异步执行时Spring容器中其他资源的安全
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 配置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁
        // 以确保应用最后能够被关闭，而不是阻塞住
        taskExecutor.setAwaitTerminationSeconds(60);
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor errorHandleExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //配置核心线程池数
        taskExecutor.setCorePoolSize(10);
        //配置最大线程数
        taskExecutor.setMaxPoolSize(50);
        //配置队列大小
        taskExecutor.setQueueCapacity(200);
        //配置空闲线程存活时间
        taskExecutor.setKeepAliveSeconds(60);
        //配置线程池中的线程名称前缀
        taskExecutor.setThreadNamePrefix("error-handle-");
        //配置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        //用于保证异步执行时Spring容器中其他资源的安全
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 配置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁
        // 以确保应用最后能够被关闭，而不是阻塞住
        taskExecutor.setAwaitTerminationSeconds(60);
        return taskExecutor;
    }

    @Bean("scheduleExecutor")
    public Executor calStateExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //配置核心线程池数
        taskExecutor.setCorePoolSize(20);
        //配置最大线程数
        taskExecutor.setMaxPoolSize(50);
        //配置队列大小
        taskExecutor.setQueueCapacity(200);
        //配置空闲线程存活时间
        taskExecutor.setKeepAliveSeconds(60);
        //配置线程池中的线程名称前缀
        taskExecutor.setThreadNamePrefix("schedule-");
        //配置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        //用于保证异步执行时Spring容器中其他资源的安全
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 配置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁
        // 以确保应用最后能够被关闭，而不是阻塞住
        taskExecutor.setAwaitTerminationSeconds(60);
        return taskExecutor;
    }




}
