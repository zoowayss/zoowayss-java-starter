package io.github.zoowayss.starter.domain.dto;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 11/29/24 14:47
 */

public class RequestHolder {

    private static final ThreadLocal<TokenUser> REQUEST_USER_INFO = new ThreadLocal<>();

    private static final ThreadLocal<Boolean> API_ENCRYPT_HOLDER = new ThreadLocal<>();

    public static void set(TokenUser requestUserInfo) {
        REQUEST_USER_INFO.set(requestUserInfo);
    }

    public static TokenUser get() {
        return REQUEST_USER_INFO.get();
    }

    public static void remove() {
        REQUEST_USER_INFO.remove();
    }

    public static void setApiNeedEncrypt() {
        API_ENCRYPT_HOLDER.set(true);
    }

    public static boolean encrypted() {
        return API_ENCRYPT_HOLDER.get() != null && API_ENCRYPT_HOLDER.get();
    }

    public static void apiEncryptContextRemove() {
        API_ENCRYPT_HOLDER.remove();
    }
}
