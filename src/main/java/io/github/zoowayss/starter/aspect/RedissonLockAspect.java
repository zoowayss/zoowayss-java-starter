package io.github.zoowayss.starter.aspect;

import io.github.zoowayss.starter.anno.RedissonLock;
import io.github.zoowayss.starter.service.LockService;
import io.github.zoowayss.starter.utils.SpElUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * Description: 分布式锁切面
 * Author: <a href="https://github.com/zoowayss">zoowayss</a>
 * @since 2023-04-20
 */
@Slf4j
@Aspect
@Order(0)// 确保比事务注解先执行，分布式锁在事务外
public class RedissonLockAspect {

    @Resource
    private LockService lockService;

    @Value("${z.starter.lock.prefix}")
    private String lockPrefix;

    @Around("@annotation(io.github.zoowayss.starter.anno.RedissonLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
        String prefix = StringUtils.hasText(redissonLock.prefixKey()) ? lockPrefix + redissonLock.prefixKey() : SpElUtils.getMethodKey(method);
        String key = SpElUtils.parseSpEl(method, joinPoint.getArgs(), redissonLock.key());
        return lockService.executeWithLockThrows(prefix + ":" + key, redissonLock.waitTime(), redissonLock.unit(), joinPoint::proceed);
    }
}
