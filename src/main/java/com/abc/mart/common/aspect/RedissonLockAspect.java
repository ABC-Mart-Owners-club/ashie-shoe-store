package com.abc.mart.common.aspect;

import com.abc.mart.common.annotation.RedissonLock;
import com.abc.mart.common.utils.CustomSpringELParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(com.abc.mart.common.annotation.RedissonLock)")
    public void redissonLock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock annotation = method.getAnnotation(RedissonLock.class);
        String lockKey = method.getName() + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), annotation.key());

        RLock lock = redissonClient.getLock(lockKey);

        boolean lockAcquired = false;

        try {
            lockAcquired = lock.tryLock(annotation.waitTime(), annotation.leaseTime(), TimeUnit.MILLISECONDS);
            if (!lockAcquired) {
                log.warn("Failed to acquire lock for key: {}", lockKey);
                return;
            }

            log.info("Lock acquired for key: {}", lockKey);

            joinPoint.proceed();

        } catch (InterruptedException e) {
            log.error("Thread was interrupted while attempting to acquire lock for key: {}", lockKey, e);
            Thread.currentThread().interrupt(); //현재 스레드에 인터럽트 요청 설정
            throw e;
        } finally {
            if (lockAcquired) {
                lock.unlock();
                log.info("Lock released for key: {}", lockKey);
            }
        }

    }

}