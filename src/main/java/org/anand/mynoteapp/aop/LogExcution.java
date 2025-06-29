package org.anand.mynoteapp.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogExcution {

    // for controllers
    @Before("execution(* org.anand.mynoteapp.controller..*(..))")
	public void beforeController(JoinPoint joinPoint)
	{
		Signature signature = joinPoint.getSignature();
		String className = signature.getDeclaringType().getSimpleName();
		String methodName = signature.getName();
		log.info("Calling :: {} :: {}()",className,methodName);
	}
    @After("execution(* org.anand.mynoteapp.controller..*(..))")
    public void afterController(JoinPoint joinPoint)
    {
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        log.info("Calling :: {} :: {}()",className,methodName);
    }

    @Around("execution(* org.anand.mynoteapp.controller..*(..))")
    public Object jointPointController(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime=System.currentTimeMillis();
        Object result=joinPoint.proceed();
        long endTime=System.currentTimeMillis();
        long totalTime=endTime-startTime;
        System.out.println("Execution time of " + joinPoint.getSignature() + " : " + totalTime + "ms");
        return result;
    }

    // also use pointcut for centralize
    @Pointcut("execution(* org.anand.mynoteapp.service..*(..))")
    public void serviceMehtod() {}


    @Before("serviceMehtod()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("calling method: " + joinPoint.getSignature());
    }
    @After("serviceMehtod()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("after calling method: " + joinPoint.getSignature());
    }

    @Around("serviceMehtod()")
    public Object logExcutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time of " + joinPoint.getSignature() + " : " + (endTime - startTime) + "ms");
        return result;
    }
}
