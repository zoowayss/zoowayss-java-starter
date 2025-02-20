package io.github.zoowayss.starter.service.factory;

import io.github.zoowayss.starter.enums.LoginMethod;
import io.github.zoowayss.starter.service.factory.handlers.loginmetod.LoginMethodHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 1/3/25 10:12
 */
@Component
public class LoginMethodFactory {

    public static final Map<LoginMethod, LoginMethodHandler> handlerMap = new HashMap<>();

    public static LoginMethodHandler getHandler(LoginMethod loginMethod) {
        return handlerMap.get(loginMethod);
    }

    @Autowired
    public void setHandlerMap(List<LoginMethodHandler> handlers) {
        Map<LoginMethod, LoginMethodHandler> map = handlers.stream().collect(Collectors.toMap(k -> LoginMethod.valueOf(k.getName()), Function.identity()));
        handlerMap.putAll(map);
    }
}
