package io.github.zoowayss.starter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * ClientSys 枚举类
 *
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 10/29/24 12:03
 */
@Getter
@AllArgsConstructor
public enum Clients {

    h5(1), ios(2), android(3);

    private static final Map<Integer, Clients> CODE_MAP = Arrays.stream(Clients.values())
                                                                .collect(Collectors.toMap(Clients::getCode, Function.identity()));

    private final int code;

    public static Clients of(int code) {
        return Optional.ofNullable(CODE_MAP.get(code))
                       .orElseThrow(() -> new IllegalArgumentException("Code of ClientSys not exist: " + code));
    }

    public static Clients ofNullable(int code) {
        return CODE_MAP.get(code);
    }
}
