package com.yuzhouwan.site.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：spring.aop
 *
 * @author Benedict Jin
 * @since 2015/11/9
 */
@Aspect
@Component
public class SpringAOPWithAspectJ {

    @Before("execution(* com.yuzhouwan.site.aop.TargetAOP.targetBefore(..))")
    public void beforeTarget(JoinPoint joinPoint) {

        showClassMethod(joinPoint);
    }

    @After("execution(* com.yuzhouwan.site.aop.TargetAOP.targetAfter(..))")
    public void afterTarget(JoinPoint joinPoint) {

        showClassMethod(joinPoint);
    }

    @AfterReturning(
            pointcut = "execution(* com.yuzhouwan.site.aop.TargetAOP.targetAfterReturning(..))",
            returning = "_return")
    public void afterReturnTarget(JoinPoint joinPoint, boolean _return) {

        showClassMethod(joinPoint);
        System.out.println("Return: " + _return);
    }

    @Around("execution(* com.yuzhouwan.site.aop.TargetAOP.targetAround(..))")
    public void aroundTarget(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("Before...");
        showClassMethod(joinPoint);
        joinPoint.proceed();
        System.out.println("After...");
    }

    @AfterThrowing(
            pointcut = "execution(* com.yuzhouwan.site.aop.TargetAOP.targetAfterThrowing(..))",
            throwing = "error")
    public void afterThrowingTarget(JoinPoint joinPoint, Throwable error) throws Throwable {

        Object[] args = joinPoint.getArgs();
        Throwable t = (Throwable) args[0];
        System.out.println("Get param value: " + t.getMessage() + " from JoinPoint...");

        showClassMethod(joinPoint);
        if (error != null)
            System.out.println("error: " + error.getMessage());
    }

    private void showClassMethod(JoinPoint joinPoint) {
        System.out.println(joinPoint.getTarget().getClass().getSimpleName() + "\'s " + joinPoint.getSignature().getName() + "...");
    }
}