package io.github.zoowayss.starter.service;


import io.github.zoowayss.starter.domain.dto.UserAddr;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 1/6/25 14:32
 */

public interface UserInfoService {

    UserAddr getUserInfo(long uid);
}
