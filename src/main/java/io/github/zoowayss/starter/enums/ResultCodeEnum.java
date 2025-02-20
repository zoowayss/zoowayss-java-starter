package io.github.zoowayss.starter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum ResultCodeEnum {
    RPC_0(0, "操作成功", "success"),

    // 登陆相关
    LOGIN_ACCESS_ACTION_IS_EXPIRED(1400010001, "Login has expired. Please login again.", ""), // token 失效
    LOGIN_FAIL_PASSWORD_FORMAT_ERROR(140001002, "Password format error", "Password format error"),
    LOGIN_FAIL_PASSPORT_ERROR_PWD_INCORRECT(140001003, "Incorrect password", "Incorrect password"),
    LOGIN_FAIL_PASSPORT_ERROR_USER_DELETED(140001004, "User already exist", "User already exist"),
    LOGIN_FAIL_PASSPORT_ERROR_EMAIL_FORMAT(140001005, "Email format error", "Email format error"),
    // passport
    USER_NOT_EXIST(1400020001, "User not exist", "User not exist"),


    // Backend
    PERMISSION_DENIED(1400030001, "Permission Denied", "Permission Denied"),

    // 限流
    FREQUENCY_LIMIT(1400040001, "To many request", "To many request");

    public final int code;

    /**
     *
     */
    public final String message;

    /**
     * 英文信息
     */
    public final String enMessage;
}
