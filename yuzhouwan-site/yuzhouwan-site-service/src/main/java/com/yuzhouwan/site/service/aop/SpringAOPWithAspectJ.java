package com.yuzhouwan.site.service.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Spring AOP with AspectJ
 *
 * @author Benedict Jin
 * @since 2015/11/9
 */
@Aspect
@Component
public class SpringAOPWithAspectJ {

    @Before("execution(* com.yuzhouwan.site.service.aop.TargetAOP.targetBefore(..))")
    public void beforeTarget(JoinPoint joinPoint) {

        showClassMethod(joinPoint);
    }

    @After("execution(* com.yuzhouwan.site.service.aop.TargetAOP.targetAfter(..))")
    public void afterTarget(JoinPoint joinPoint) {

        showClassMethod(joinPoint);
    }

    @AfterReturning(
            pointcut = "execution(* com.yuzhouwan.site.service.aop.TargetAOP.targetAfterReturning(..))",
            returning = "ret")
    public void afterReturnTarget(JoinPoint joinPoint, boolean ret) {

        showClassMethod(joinPoint);
        System.out.println("Return: " + ret);
    }

    @Around("execution(* com.yuzhouwan.site.service.aop.TargetAOP.targetAround(..))")
    public void aroundTarget(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("Before...");
        showClassMethod(joinPoint);
        joinPoint.proceed();
        System.out.println("After...");
    }

    @AfterThrowing(
            pointcut = "execution(* com.yuzhouwan.site.service.aop.TargetAOP.targetAfterThrowing(..))",
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
        System.out.println(joinPoint.getTarget().getClass().getSimpleName() + "\'s "
                + joinPoint.getSignature().getName() + "...");
    }
}
