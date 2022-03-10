package com.hust.nodecontroller.aop;

import com.hust.nodecontroller.utils.CalStateUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;


/**
 * @author Zhang Bowen
 * @Description
 * @ClassName ServiceAspect
 * @date 2020.10.16 12:21
 */

@Aspect
@Component
public class ServiceAspect {
    private static final Logger logger = LoggerFactory.getLogger(ServiceAspect.class);

    @Pointcut("execution(* com.hust.nodecontroller.service.NodeServiceImpl.*(..))")
    public void pointCutNodeService() {}

    @Pointcut("execution(* com.hust.nodecontroller.communication..*.*(..))")
    public void pointCutCommunication() {}

    @Around(" pointCutCommunication()")
    public Object doAroundCommunication(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.nanoTime();
        String methodSignature = joinPoint.getSignature().toString();
        String args = Arrays.toString(joinPoint.getArgs());
        Object result = joinPoint.proceed();
        long endTime = System.nanoTime();
        logger.info("Finish the communication method({}), Total time({}ms)", methodSignature, (endTime-beginTime)/1000000);
        return result;
    }

    @Around("pointCutNodeService() || pointCutCommunication()")
    public Object doAroundNodeService(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.nanoTime();
        String methodSignature = joinPoint.getSignature().toString();
        String args = Arrays.toString(joinPoint.getArgs());
        Object result = joinPoint.proceed();
        long endTime = System.nanoTime();
        logger.info("Finish the service method({}), Total time({}ms)", methodSignature, (endTime-beginTime)/1000000);
        return result;
    }

}
