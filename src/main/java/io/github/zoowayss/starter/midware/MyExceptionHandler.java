package io.github.zoowayss.starter.midware;

import io.github.zoowayss.starter.domain.vo.ResultVo;
import io.github.zoowayss.starter.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultVo<?> exceptionHandler(Exception e) {
        log.error("Global unknown error:", e);
        return ResultVo.asError(-1, "system error", null, "");
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultVo<?> processBusinessException(BusinessException e) {
        log.error("Global BusinessException:", e);
        return ResultVo.asError(e.getCode(), e.getMessage(), e.getPayload(), "");
    }

}