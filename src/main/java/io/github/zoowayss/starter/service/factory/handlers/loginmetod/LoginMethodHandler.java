package io.github.zoowayss.starter.service.factory.handlers.loginmetod;

import io.github.zoowayss.starter.domain.req.LoginReq;
import io.github.zoowayss.starter.service.NamedHandler;
import org.springframework.util.StringUtils;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 1/3/25 10:14
 */

public interface LoginMethodHandler extends NamedHandler {

    String doLogin(LoginReq loginReq);

    /**
     * 检测邮箱格式是否合法
     * @param email 邮箱
     * @return true: 格式合法
     */
    static boolean checkEmailFormat(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String[] emailSplit = email.split("@");
        if (emailSplit.length != 2) {
            return false;
        }
        return true;
    }


    /**
     * 检测密码格式是否合法
     *
     * @param password 密码
     * @return true: 格式不合法
     */
    static boolean checkPasswordFormat(String password) {
        return !StringUtils.hasText(password) || password.length() < 6 || password.length() > 50;
    }
}
