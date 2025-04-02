package com.javaquasar.cache.aop;

import com.javaquasar.cache.util.MillisecondsFormatter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingInRestServicesAspect {

    @Around("execution(* com.javaquasar.cache.controller.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();

        log.info("Started: {}", methodName);
        try {
            Object[] args = joinPoint.getArgs();
            log.info("➡️  Incoming request: {} with args: {}", methodName, Arrays.toString(args));

            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("⬅️  Response from: {} = {} ({} ms)", methodName, result, MillisecondsFormatter.format(duration));
            return result;
        } catch (Throwable t) {
            log.error("Exception in {}: {}", methodName, t.getMessage());
            throw t;
        }
    }

}
