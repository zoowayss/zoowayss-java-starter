package io.github.zoowayss.starter.service.factory.handlers.loginmetod;

import io.github.zoowayss.starter.anno.RedissonLock;
import io.github.zoowayss.starter.domain.dto.UserAddr;
import io.github.zoowayss.starter.domain.req.LoginReq;
import io.github.zoowayss.starter.enums.LoginMethod;
import io.github.zoowayss.starter.enums.ResultCodeEnum;
import io.github.zoowayss.starter.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 1/3/25 10:12
 */
@Service
public class EmailPwdLoginMethodHandler extends AbstractLoginMethodHandler {


    @Override
    public String doLogin(LoginReq loginReq) {

        if (!LoginMethodHandler.checkEmailFormat(loginReq.getAccount())) {
            throw new BusinessException(ResultCodeEnum.LOGIN_FAIL_PASSPORT_ERROR_EMAIL_FORMAT);
        }
        return super.doLogin(loginReq);
    }


    @Override
    protected UserAddr getCurrentUser(LoginReq loginReq) {
        return userService.getByEmail(loginReq.getAccount());
    }


    @RedissonLock(prefixKey = ":login:register:email", key = "#loginReq.getAccount()", waitTime = 10, unit = TimeUnit.SECONDS)
    @Override
    protected long registerNewUser(LoginReq loginReq) {
        UserAddr savedUser = new UserAddr();
        savedUser.setEmail(loginReq.getAccount());
        savedUser.setPassword(passwordEncoder.encode(loginReq.getPassword()));
        return userService.registerNewUser(savedUser);
    }

    @Override
    public String getName() {
        return LoginMethod.EMAIL_PWD.name();
    }
}
