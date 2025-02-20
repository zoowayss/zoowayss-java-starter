package io.github.zoowayss.starter.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.zoowayss.starter.domain.dto.TokenUser;
import io.github.zoowayss.starter.enums.ResultCodeEnum;
import io.github.zoowayss.starter.exception.BusinessException;
import io.github.zoowayss.starter.service.TokenService;
import io.github.zoowayss.starter.service.UserService;
import io.github.zoowayss.starter.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 12/4/24 11:48
 */
@Service
@Slf4j
public class JwtTokenServiceImpl implements TokenService {

    @Resource
    private UserService userService;

    @Override
    public String generateToken(Long uid) {

        TokenUser info = userService.getRequestUserInfo(uid);
        if (Objects.isNull(info)) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_EXIST);
        }
        return JWT.create()
                  .withClaim("uid", uid)
                  .withClaim("did", info.getDid())
                  .withClaim("email", info.getEmail())
                  .withClaim("exp", TimeUtils.getCurrentTimeMils() + EXPIRE_TIME)
                  .sign(Algorithm.HMAC256(secretKey));
    }

    @Override
    public TokenUser verify(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                return null;
            }
            return parseToken(token);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("{} parse token failed", token);
        }
        return null;
    }

    /**
     * 解析token.
     * @param token token
     * @return TokenUser
     */
    public TokenUser parseToken(String token) {
        DecodedJWT decodedjwt = JWT.require(Algorithm.HMAC256(secretKey))
                                   .build().verify(token);
        Claim userId = decodedjwt.getClaim("uid");
        Claim did = decodedjwt.getClaim("did");
        Claim email = decodedjwt.getClaim("email");
        Claim exp = decodedjwt.getClaim("exp");

        if (StringUtils.hasText(email.asString()) && exp.asLong() < TimeUtils.getCurrentTimeMils()) {
            throw new BusinessException(ResultCodeEnum.LOGIN_ACCESS_ACTION_IS_EXPIRED);
        }

        return TokenUser.builder().uid(userId.asLong())
                                  .did(did.asString())
                                  .email(email.asString())
                                  .build();
    }
}
