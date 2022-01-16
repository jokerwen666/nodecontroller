package com.hust.nodecontroller.aop;

import com.hust.nodecontroller.utils.IndustryQueryUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.hust.nodecontroller.utils.CalStateUtil;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName CalStateAspect
 * @date 2020.10.17 18:46
 */

@Aspect
@Component
public class CalStateAspect {

    @Pointcut("execution(* com.hust.nodecontroller.controller.NodeController.register(..))")
    public void pointCutRegister() {}

    @Before("pointCutRegister()")
    public void doBeforeRegister() throws Exception {
        CalStateUtil.registerCount++;
        CalStateUtil.totalCount++;
    }


    @Pointcut("execution(* com.hust.nodecontroller.controller.NodeController.query(..))")
    public void pointCutQuery() {}

    @Before("pointCutQuery()")
    public void doBeforeQuery() throws Exception {
        CalStateUtil.queryCount++;
        IndustryQueryUtil.setQueryCount(IndustryQueryUtil.getQueryCount()+1);
        CalStateUtil.totalCount++;
    }


    @Pointcut("execution(* com.hust.nodecontroller.controller.NodeController.update(..))")
    public void pointCutUpdate() {}

    @Before("pointCutUpdate()")
    public void doBeforeUpdate() throws Exception {
        CalStateUtil.totalCount++;
    }


    @Pointcut("execution(* com.hust.nodecontroller.controller.NodeController.delete(..))")
    public void pointCutDelete() {}

    @Before("pointCutDelete()")
    public void doBeforeDelete() throws Exception {
        CalStateUtil.totalCount++;
    }

}
