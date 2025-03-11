package io.github.zoowayss.starter.service.factory.handlers.loginmetod;

import io.github.zoowayss.starter.domain.dto.UserAddr;
import io.github.zoowayss.starter.domain.req.LoginReq;
import io.github.zoowayss.starter.enums.ResultCodeEnum;
import io.github.zoowayss.starter.exception.BusinessException;
import io.github.zoowayss.starter.listener.event.AfterLoginAsyncEvent;
import io.github.zoowayss.starter.service.TokenService;
import io.github.zoowayss.starter.service.UserRegisterPostProcessor;
import io.github.zoowayss.starter.service.UserService;
import io.github.zoowayss.starter.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 1/3/25 10:31
 */
@Slf4j
@EnableAspectJAutoProxy(exposeProxy = true)
public abstract class AbstractLoginMethodHandler implements LoginMethodHandler {

    @Resource
    protected UserService userService;

    @Resource
    protected ApplicationEventPublisher eventPublisher;

    @Resource
    private TokenService tokenService;

    @Resource
    private List<UserRegisterPostProcessor> userRegisterPostProcessors;

    protected final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    @Override
    public String doLogin(LoginReq loginReq) {

        // 密码，密码需使用8到15个字符（字母、数字和符号的组合）
        if (LoginMethodHandler.checkPasswordFormat(loginReq.getPassword())) {
            throw new BusinessException(ResultCodeEnum.LOGIN_FAIL_PASSWORD_FORMAT_ERROR);
        }

        UserAddr currentUser = getCurrentUser(loginReq);
        if (Objects.isNull(currentUser)) {
            // 自动注册
            long userId = this.registerNewUser(loginReq);
            for (UserRegisterPostProcessor processor : userRegisterPostProcessors) {
                if (!processor.postProcess(userId)) {
                    log.warn("{} postProcess failed, uid: {}", processor.getClass().getSimpleName(), userId);
                }
            }
            return generateToken(userId);
        }

        // 已经存在的用户；登陆校验
        if (currentUser.isDeleted()) {
            throw new BusinessException(ResultCodeEnum.LOGIN_FAIL_PASSPORT_ERROR_USER_DELETED);
        }

        if (!matchPassword(loginReq, currentUser)) {
            throw new BusinessException(ResultCodeEnum.LOGIN_FAIL_PASSPORT_ERROR_PWD_INCORRECT);
        }

        eventPublisher.publishEvent(new AfterLoginAsyncEvent(this, currentUser.getUid(), ServletUtils.getClientIp()));
        return generateToken(currentUser.getUid());
    }

    protected abstract UserAddr getCurrentUser(LoginReq loginReq);

    protected abstract long registerNewUser(LoginReq loginReq);

    protected String generateToken(long uid) {
        return tokenService.generateToken(uid);
    }

    protected boolean matchPassword(LoginReq loginReq, UserAddr currentUser) {
        return passwordEncoder.matches(loginReq.getPassword(), currentUser.getPassword());
    }
}
