package io.github.zoowayss.starter.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ResultCodeEnum {
    RPC_0(0, "操作成功", "success"),

    // 登陆相关
    LOGIN_ACCESS_ACTION_IS_EXPIRED(1401010001, "Login has expired. Please login again.", ""), // token 失效
    PASSPORT_ERROR_LOGIN_FAIL_PASSWORD_Format_ERROR(10007003, "Password format error", "Password format error"),

    // File upload
    UPLOAD_TOKEN_ERROR(144141107, "Get token internal error", "Get token internal error"),
    MEDIA_UPLOAD_REQUEST_ERROR(1441415005, "Failed to upload file request", "Failed to upload file request"),
    MEDIA_UPLOAD_FILE_NOT_EXISTS(1441415013, "Error uploading file parameters", "Error uploading file parameters"),
    MEDIA_URL_UPLOAD_SIZE_ERROR(1441415010, "File size exceeded", "File size exceeded"),
    MEDIA_UPLOAD_WIDTH_HEIGHT_EXCEED(1441415014, "Image width and height exceeded the limit", "Image width and height exceeded the limit"),

    MEDIA_UPLOAD_IMAGE_TYPE_ERROR(1441415008, "File type error", "File type error"),
    MEDIA_URL_UPLOAD_PARAM_EMPTY(1441415009, "The file URL cannot be empty", "The file URL cannot be empty"),
    MEDIA_URL_UPLOAD_MEDIATYPE_ERROR(1441415011, "File type error", "File type error"),
    MEDIA_URL_UPLOAD_STATUS_ERROR(1441415012, "Remote file response failed", "Remote file response failed"),
    MEDIA_UPLOAD_TEMP_FILE_FAIL(1441415007, "Failed to upload file request", "Failed to upload file request"),
    MEDIA_UPLOAD_REQUEST_FAIL(1441415006, "Failed to upload file request", "Failed to upload file request"),

    // passport
    PASSPORT_ERROR_GOOGLE_TOKEN_WRONG(10007019, "Google login mismatch", "Google login mismatch"),
    PASSPORT_ERROR_LOGIN_FAIL(10007002, "Login failed, try again later.", "Login failed, try again later."),
    PASSPORT_ERROR_FACEBOOK_SHORT_TOKEN_NEED(10007021, "Short token needed", "Short token needed"),
    PASSPORT_ERROR_FACEBOOK_USER_ID_NEED(10007022, "Facebook User id needed", "Facebook User id Needed"),
    PASSPORT_ERROR_GOOGLE_TOKEN_NEED(10007018, "Google login failed", "Google Login failed"),
    PASSPORT_ERROR_FACEBOOK_USER_ID_WRONG(10007023, "Facebook user id get failed", "Facebook user id get failed"),
    PASSPORT_ERROR_FACEBOOK_LOGIN_FAILED(10007024, "Facebook login failed", "Facebook login failed"),
    LOGIN_BIND_ERROR_EMAIL_ALREADY_EXIST(10007025, "Email already exist"),
    LOGIN_BIND_ERROR_TO_BIND_EMAIL(10007026, "Please bind email first", "Please bind email first"),
    USER_NOT_EXIST(10007027, "User not exist", "User not exist"),
    USER_NOT_FOUND_EMAIL(10007034, "User email not found", "User email not found"),


    PASSPORT_ERROR_PWD_INCORRECT(10007035, "Incorrect password", "Incorrect password"),
    PASSPORT_ERROR_USER_DELETED(10007036, "User not exist", "User not exist"),
    PASSPORT_ERROR_EMAIL_FORMAT(10007037, "Email format error", "Email format error"),
    NEED_BIND_EMAIL(10007038, "Need bind email", "Need bind email"),

    // 默认失败
    LOGIN_ERROR(1407010000, "Login failed", "Login failed"),
    // 没有传登录态
    TO_LOGIN(1407010001, "Please login first", "Please login first"),
    // 传过来的refreshToken失效了，超过登陆有效期
    LOGIN_REFRESH_TOKEN_IS_EXPIRED(1407010005, "登陆已失效，请重新登陆"),
    // 调用登录态GRPC接口失败了
    LOGIN_VERIFY_FAIL(1407010006, "Login failed", "Login failed"),
    // 传过来的refreshToken解密失败了
    LOGIN_REFRESH_TOKEN_NOT_LEGAL(1407010007, "Parameters are incorrect, please re login", "Parameters are incorrect, please re login"),


    // GPT
    INVALID_MESSAGE_NUM(1441403001, "invalid message num", "invalid message num"),
    GTP_REQUEST_FAIL(1441403002, "Failed to gpt request", "Failed gpt request"),
    INVALID_AI_EXPERT_MESSAGE(1441403003, "invalid ai expert message", "invalid ai expert message"),

    // payment

    SKU_NOT_FOUND(1441412004, "Sku not found", "Sku not found"),
    ORDER_NOT_PAID(1441412005, "Order not paid", "Order not paid"),
    ORDER_IS_PROCESSING(1441412006, "Order is processing", "Order is processing"),


    FREQUENCY_LIMIT(100001000, "Please try again later", "Please try again later"),
    FREQUENCY_CONTROL_KEY_NOT_ALLOW_NULL(100001001, "Frequency NULL key", "Frequency NULL key"),


    // Backend
    PERMISSION_DENIED(100002000, "Permission Denied", "Permission Denied"),
    ;

    public int code;

    /**
     *
     */
    public String message;

    /**
     * 英文信息
     */
    public String enMessage;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    ResultCodeEnum(int code, String message, String enMessage) {
        this.code = code;
        this.message = message;
        this.enMessage = enMessage;
    }
}
