package io.github.zoowayss.starter.aspect;

import io.github.zoowayss.starter.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Aspect
@Component
@Order(1)
@Slf4j
public class RequestLoggingAspect {

    private final HttpServletRequest request;

    public RequestLoggingAspect(HttpServletRequest request) {
        this.request = request;
    }

    // 在Controller方法执行之前打印请求信息
    @Before("execution(* *..controller.*.*(..))") // 这里可以根据需要调整路径
    public void logRequestDetails(JoinPoint joinPoint) {
        String method = request.getMethod();  // 获取HTTP方法（GET, POST等）
        String url = request.getRequestURI(); // 获取请求的URI
        String clientIp = request.getRemoteAddr(); // 获取客户端IP

        Map<String, Object> requestParams = new HashMap<>();

        requestParams.put("ip", clientIp);
        requestParams.put("method", method);
        requestParams.put("uri", url);
        // 打印方法名和参数
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) continue; // 忽略HttpServletRequest参数
            if (arg instanceof HttpServletResponse) continue; // 忽略HttpServletResponse参数
            if (arg instanceof BindingResult) continue; // 忽略BindingResult参数
            if (Objects.nonNull(arg)) {
                requestParams.put(arg.getClass().getSimpleName(), arg);
            }
        }

        log.info("Request: {}", JsonUtils.toJson(requestParams));
    }

    // 在Controller方法执行之后打印返回值（如果需要）
    @AfterReturning(pointcut = "execution(* *..controller.*.*(..))", returning = "result")
    public void logResponseDetails(Object result) {
        log.info("Response: {}", JsonUtils.toJson(result));
    }
}
