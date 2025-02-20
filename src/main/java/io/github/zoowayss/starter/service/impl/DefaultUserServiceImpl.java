package io.github.zoowayss.starter.service.impl;

import io.github.zoowayss.starter.domain.dto.TokenUser;
import io.github.zoowayss.starter.domain.dto.UserAddr;
import io.github.zoowayss.starter.service.UserInfoService;
import io.github.zoowayss.starter.service.UserService;

import java.util.*;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 1/3/25 11:02
 */

public class DefaultUserServiceImpl implements UserService, UserInfoService {

    public static final TokenUser DEFAULT_USER_INFO = new TokenUser(1L, "default_did", "1.1.1.1", "test@email.com");

    private static final Map<Long, UserAddr> datasource = new HashMap<>();

    @Override
    public TokenUser getRequestUserInfo(Long uid) {
        return datasource.get(uid);
    }

    @Override
    public UserAddr getByEmail(String account) {
        return datasource.values().stream().filter(userInfo -> userInfo.getEmail().equals(account)).findFirst().orElse(null);
    }

    @Override
    public long registerNewUser(UserAddr savedUser) {

        long maxId = 0;
        for (Long uid : datasource.keySet()) {
            if (uid > maxId) {
                maxId = uid;
            }
        }

        savedUser.setUid(++maxId);
        datasource.put(savedUser.getUid(), savedUser);
        return maxId;
    }

    @Override
    public boolean matchPassword(String source, String encrypt) {
        return true;
    }

    @Override
    public void deleteAccount(Long uid) {

    }

    @Override
    public UserAddr getUserInfo(long uid) {
        return datasource.get(uid);
    }
}
