package io.github.zoowayss.starter.midware;

import io.github.zoowayss.starter.domain.dto.RequestHolder;
import io.github.zoowayss.starter.utils.AESUtils;
import io.github.zoowayss.starter.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 返回数据加密
 *
 * @since 2022/11/8 14:47
 */
@Slf4j
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {

        return RequestHolder.encrypted();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String cm = "EncryptResponseBodyAdvice";
        String path = serverHttpRequest.getURI().getPath();
        String content = JsonUtils.toJson(body);
        String result = AESUtils.encrypt(JsonUtils.toJson(body));
        log.info("{} uri: {} Pre-encrypted data：{}，After encryption：{}", cm, path, content, result);
        return result;
    }
}
