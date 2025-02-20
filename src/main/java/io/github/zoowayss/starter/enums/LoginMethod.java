package io.github.zoowayss.starter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * LoginMethod 枚举类
 *
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 10/29/24 12:03
 */
@Getter
@AllArgsConstructor
public enum LoginMethod {

    /**
     * GOOGLE 第三方登陆
     */
    GOOGLE(1),

    /**
     * FACEBOOK 第三方登陆
     */
    FACEBOOK(2),
    /**
     * 邮箱 密码
     */
    EMAIL_PWD(3),
    /**
     * 邮箱 密码
     */
    ACCOUNT_PWD(4);

    private static final Map<Integer, LoginMethod> CODE_MAP = Arrays.stream(LoginMethod.values())
                                                                    .collect(Collectors.toMap(LoginMethod::getCode, Function.identity()));

    private final int code;

    public static LoginMethod of(int code) {
        return Optional.ofNullable(CODE_MAP.get(code))
                       .orElseThrow(() -> new IllegalArgumentException("Code of LoginMethod not exist: " + code));
    }

    public static LoginMethod ofNullable(int code) {
        return CODE_MAP.get(code);
    }
}
