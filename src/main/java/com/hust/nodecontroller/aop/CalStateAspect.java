package com.hust.nodecontroller.aop;

import com.hust.nodecontroller.utils.IndustryQueryUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import com.hust.nodecontroller.utils.CalStateUtil;

/**
 * @program nodecontroller
 * @Description 更新请求前后的系统状态变量
 * @Author jokerwen666
 * @date 2022-01-20 19:48
 **/

@Aspect
@Component
public class CalStateAspect {

    @Pointcut("execution(* com.hust.nodecontroller.controller.NodeController.register(..))")
    public void pointCutRegister() {}
    @Before("pointCutRegister()")
    public void doBeforeRegister() {
        CalStateUtil.registerCount++;
        CalStateUtil.totalCount++;
    }

    @Pointcut("execution(* com.hust.nodecontroller.controller.NodeController.update(..))")
    public void pointCutUpdate() {}
    @Before("pointCutUpdate()")
    public void doBeforeUpdate() {
        CalStateUtil.totalCount++;
    }

    @Pointcut("execution(* com.hust.nodecontroller.controller.NodeController.delete(..))")
    public void pointCutDelete() {}
    @Before("pointCutDelete()")
    public void doBeforeDelete() {
        CalStateUtil.totalCount++;
    }

    @Pointcut("execution(* com.hust.nodecontroller.service.NodeServiceImpl.multipleTypeQuery(..))")
    public void pointCutMultipleTypeQuery() {}
    @Around("pointCutMultipleTypeQuery()")
    public Object doAroundNodeService(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.nanoTime();
        CalStateUtil.queryCount++;
        IndustryQueryUtil.setQueryCount(IndustryQueryUtil.getQueryCount()+1);
        CalStateUtil.totalCount++;
        Object result = joinPoint.proceed();
        long endTime = System.nanoTime();
        CalStateUtil.queryTimeout += (endTime-beginTime)/1000000;
        return result;
    }

}
