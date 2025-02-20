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
            throw new BusinessException(ResultCodeEnum.PASSPORT_ERROR_EMAIL_FORMAT);
        }

        // 密码，密码需使用8到15个字符（字母、数字和符号的组合）
        if (LoginMethodHandler.checkPasswordFormat(loginReq.getPassword())) {
            throw new BusinessException(ResultCodeEnum.PASSPORT_ERROR_LOGIN_FAIL_PASSWORD_Format_ERROR);
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
        savedUser.setPassword(loginReq.getPassword());
        return userService.registerNewUser(savedUser);
    }

    @Override
    public String getName() {
        return LoginMethod.ACCOUNT_PWD.name();
    }
}
