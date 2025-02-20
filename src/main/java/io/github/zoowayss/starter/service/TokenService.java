package io.github.zoowayss.starter.service;


import io.github.zoowayss.starter.domain.dto.TokenUser;

import java.util.concurrent.*;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 12/4/24 11:44
 */

public interface TokenService {

    String secretKey = "s";

    // token 过期时间 10年
    long EXPIRE_TIME = TimeUnit.DAYS.toMillis(3650);

    /**
     * 生成 token
     *
     * @param uid 用户id
     * @return token
     */
    String generateToken(Long uid);

    /**
     * 验证 token
     *
     * @param token token
     * @return TokenUser
     */
    TokenUser verify(String token);
}
