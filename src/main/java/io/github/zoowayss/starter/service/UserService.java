package io.github.zoowayss.starter.service;

import io.github.zoowayss.starter.domain.dto.TokenUser;
import io.github.zoowayss.starter.domain.dto.UserAddr;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 12/4/24 11:55
 */

public interface UserService {

    TokenUser getRequestUserInfo(Long uid);

    UserAddr getByEmail(String account);

    long registerNewUser(UserAddr savedUser);

    boolean matchPassword(String source, String encrypt);

    void deleteAccount(Long uid);
}
