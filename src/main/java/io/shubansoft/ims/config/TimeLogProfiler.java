package io.shubansoft.ims.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class TimeLogProfiler {

    @Around("within(io.shubansoft.ims.controller..*)")
    public static Object logTime(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return getTimedResult(proceedingJoinPoint);
    }

    private static Object getTimedResult(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final long startNanos= System.nanoTime();
        final Object result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        final long endNanos= System.nanoTime();
        final long timeNanos = endNanos - startNanos;
        final long timeMillis = timeNanos/1000000;
        logTime(proceedingJoinPoint,timeMillis);
        return result;
    }

    private static void logTime(final JoinPoint jp, final long timeMillis){
        final String declaringTypeName = jp.getSignature().getDeclaringTypeName();
        final String methodName = jp.getSignature().getName();
        final String targetName = declaringTypeName + "." + methodName;
        log.info("targetName={}, duration={} ms",targetName,timeMillis);
    }
}
