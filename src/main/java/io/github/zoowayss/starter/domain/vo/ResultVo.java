package io.github.zoowayss.starter.domain.vo;

import io.github.zoowayss.starter.enums.ResultCodeEnum;
import io.github.zoowayss.starter.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 给前端输出数据用的Vo包裹类
 *
 * @param <T> T
 * @author optimized by gufeng
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
public class ResultVo<T> implements Serializable {

    public static final int SUCCESS_CODE = 0;

    public static final int ERROR_CODE = -1;

    public static final String SUCCESS_MSG = "success";

    public static final String ERROR_MSG = "fail";

    public static final int BAD_REQUEST_CODE = 400;

    public static final int SERVER_ERROR_CODE = 500;

    public static final int USER_FORBIDDEN_CODE = 403;

    public static final int NOT_DSP_USER_CODE = 417;

    public static final int REDIRECT_CODE = 302;

    private int error = SUCCESS_CODE;

    private String msg = SUCCESS_MSG;

    private T data;

    // private String enMsg = "";
    private String redirectUrl;

    private int isEncrypted = 0;

    public ResultVo(int error, String msg, T data, String redirectUrl) {

        this.error = error;
        this.msg = msg;
        this.data = data;
        this.redirectUrl = redirectUrl;
    }

    public static ResultVo asSuccess() {
        return asSuccess(null);
    }

    public static <T> ResultVo<T> asSuccess(T result) {
        return asSuccess(SUCCESS_MSG, result, null);
    }

    public static <T> ResultVo<T> asSuccess(String msg, T result, String redirectUrl) {
        return new ResultVo<>(SUCCESS_CODE, msg, result, redirectUrl);
    }

    public static <T> ResultVo<T> asError(String msg) {
        return asError(msg, null);
    }

    public static <T> ResultVo<T> asError(String msg, T data) {
        return asError(ERROR_CODE, msg, data, null);
    }

    public static <T> ResultVo<T> asError(Integer error, String msg, T data, String redirectUrl) {
        if (error == null) {
            error = ERROR_CODE;
        }
        return new ResultVo<>(error, msg, data, redirectUrl);
    }

    /**
     * 处理错误数据结构
     *
     * @param be ex
     * @param <T> T
     * @return ResultVo
     */
    public static <T> ResultVo<T> asError(BusinessException be) {
        if (be.getRce() != null) {
            return with(be.getRce());
        }
        return asError(be.getCode(), be.getMessage());
    }

    public static <T> ResultVo<T> with(ResultCodeEnum em) {
        return new ResultVo<>(em.code, em.message, null, null);
    }

    public static <T> ResultVo<T> asError(Integer code, String msg) {
        if (code == null) {
            code = ERROR_CODE;
        }
        return asError(code, msg, null, null);
    }

    public static <T> ResultVo<T> asErrorAndRedirect(String redirectUrl) {
        return new ResultVo<>(900, "", null, redirectUrl);
    }

    public static <T> ResultVo<T> asErrorWithCode(int code, String msg) {
        return new ResultVo<>(code, msg, null, null);
    }

    public static <T> ResultVo<T> asDeniedAccess(String redirectUrl) {
        return new ResultVo<>(USER_FORBIDDEN_CODE, "user has no right to access", null, redirectUrl);
    }

    public static <T> ResultVo<T> asRedirect(String redirectUrl) {
        return new ResultVo<>(REDIRECT_CODE, "redirect", null, redirectUrl);
    }

    public boolean success() {
        return this.getError() == SUCCESS_CODE;
    }
}
