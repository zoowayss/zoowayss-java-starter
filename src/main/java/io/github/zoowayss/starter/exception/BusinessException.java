package io.github.zoowayss.starter.exception;

import io.github.zoowayss.starter.enums.ResultCodeEnum;
import lombok.Getter;
import lombok.Setter;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -9043928152358520489L;

    protected String originalMessage;

    @Getter
    @Setter
    protected Integer code;

    @Getter
    @Setter
    protected ResultCodeEnum rce;

    @Getter
    @Setter
    private Object payload;

    public BusinessException(String msg) {
        super(msg);
        this.code = -1;
        this.originalMessage = msg;
    }

    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
        this.originalMessage = msg;
        this.code = -1;
    }

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
        code = null;
    }

    public BusinessException(ResultCodeEnum resultCodeEnum, Object payload) {
        this(resultCodeEnum);
        this.payload = payload;
    }

    public BusinessException(ResultCodeEnum em) {
        super(em.message);
        this.originalMessage = em.message;
        this.code = em.getCode();
        this.rce = em;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }
}
