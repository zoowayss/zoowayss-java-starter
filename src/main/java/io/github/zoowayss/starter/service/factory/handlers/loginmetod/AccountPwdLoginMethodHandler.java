package io.github.zoowayss.starter.service.factory.handlers.loginmetod;

import io.github.zoowayss.starter.anno.RedissonLock;
import io.github.zoowayss.starter.domain.dto.UserAddr;
import io.github.zoowayss.starter.domain.req.LoginReq;
import io.github.zoowayss.starter.enums.LoginMethod;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 3/7/25 15:38
 */
@Service
public class AccountPwdLoginMethodHandler extends AbstractLoginMethodHandler {

    @Override
    protected UserAddr getCurrentUser(LoginReq loginReq) {
        return userService.getByName(loginReq.getAccount());
    }


    @RedissonLock(prefixKey = ":login:register:email", key = "#loginReq.getAccount()", waitTime = 10, unit = TimeUnit.SECONDS)
    @Override
    protected long registerNewUser(LoginReq loginReq) {
        UserAddr savedUser = new UserAddr();
        savedUser.setAccount(loginReq.getAccount());
        savedUser.setPassword(passwordEncoder.encode(loginReq.getPassword()));
        return userService.registerNewUser(savedUser);
    }

    @Override
    public String getName() {
        return LoginMethod.ACCOUNT_PWD.name();
    }
}
