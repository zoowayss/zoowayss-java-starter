package io.github.zoowayss.starter.midware;

import io.github.zoowayss.starter.anno.TokenOptional;
import io.github.zoowayss.starter.domain.dto.TokenUser;
import io.github.zoowayss.starter.service.TokenService;
import io.github.zoowayss.starter.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 2/12/25 10:49
 */
@Order(-2)
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    public static final String AUTHORIZATION_FAILED = "{\"error\":403,\"msg\":\"Permission Denied\",\"data\":{},\"redirectUrl\":null,\"isEncrypted\":0}";

    public static final String ATTRIBUTE_REQUEST_USER_INFO = "request_user_info";

    @Resource
    private TokenService tokenService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否是方法处理请求
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            String token = ServletUtils.appAuthorization();
            if (StringUtils.hasText(token)) {
                token = token.replace("Bearer ", "");
            }

            if (!StringUtils.hasText(token)) {
                // 执行认证 从 http 请求头中取出 token
                Cookie cookie = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[]{new Cookie("x", "")}))
                                      .filter(c -> Objects.equals(c.getName(), HttpHeaders.AUTHORIZATION)).findFirst().orElse(new Cookie("x", ""));
                token = cookie.getValue();
            }

            TokenUser requestUserInfo = TokenUser.builder().did(ServletUtils.appDeviceId()).email("").uid(0L).ip(ServletUtils.getClientIp()).build();
            // 遍历方法的所有参数，检查是否有 @Required 注解
            for (Parameter parameter : handlerMethod.getMethod().getParameters()) {
                if (parameter.getType() == TokenUser.class) {
                    // 需要验证 Token
                    TokenUser tokenUser = tokenService.verify(token);

                    if (Objects.isNull(tokenUser)) {
                        if(!parameter.isAnnotationPresent(TokenOptional.class)){
                            return permissionDenied(response);
                        }
                        request.setAttribute(ATTRIBUTE_REQUEST_USER_INFO, requestUserInfo);
                        break;
                    }
                    // 将解析出的用户信息存入 request，供 Controller 使用
                    request.setAttribute(ATTRIBUTE_REQUEST_USER_INFO, tokenUser);
                    break;
                }
            }
        }
        return true;
    }


    private boolean permissionDenied(HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.getWriter().write(AUTHORIZATION_FAILED);
        response.getWriter().flush();
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
    }
}
