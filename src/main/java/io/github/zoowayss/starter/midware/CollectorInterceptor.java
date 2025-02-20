package io.github.zoowayss.starter.midware;

import io.github.zoowayss.starter.domain.dto.RequestHolder;
import io.github.zoowayss.starter.domain.dto.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 信息收集的拦截器
 */
@Order(1)
@Slf4j
public class CollectorInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object attribute = request.getAttribute(TokenInterceptor.ATTRIBUTE_REQUEST_USER_INFO);
        if (Objects.nonNull(attribute) && attribute instanceof TokenUser) {
            RequestHolder.set((TokenUser) attribute);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove();
    }

}