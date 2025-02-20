package io.github.zoowayss.starter.service.factory.handlers.loginmetod;

import io.github.zoowayss.starter.domain.dto.UserAddr;
import io.github.zoowayss.starter.domain.req.LoginReq;
import io.github.zoowayss.starter.enums.LoginMethod;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 1/3/25 16:21
 */
@Service
public class GoogleLoginMethodHandler extends AbstractLoginMethodHandler {

    @Override
    public String getName() {
        return LoginMethod.GOOGLE.name();
    }

    @Override
    public String doLogin(LoginReq loginReq) {
        return null;
    }

    @Override
    protected UserAddr getCurrentUser(LoginReq loginReq) {
        return null;
    }

    @Override
    protected long registerNewUser(LoginReq loginReq) {
        return 0L;
    }
}
