package io.github.zoowayss.starter.controller;

import io.github.zoowayss.starter.domain.dto.TokenUser;
import io.github.zoowayss.starter.domain.dto.UserAddr;
import io.github.zoowayss.starter.domain.req.LoginReq;
import io.github.zoowayss.starter.domain.vo.ResultVo;
import io.github.zoowayss.starter.enums.LoginMethod;
import io.github.zoowayss.starter.exception.BusinessException;
import io.github.zoowayss.starter.service.TokenService;
import io.github.zoowayss.starter.service.UserInfoService;
import io.github.zoowayss.starter.service.UserService;
import io.github.zoowayss.starter.service.factory.LoginMethodFactory;
import io.github.zoowayss.starter.service.factory.handlers.loginmetod.LoginMethodHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 1/3/25 10:03
 */
@Slf4j
@RestController
@RequestMapping("/v1/account")
public class AccountController {

    @Resource
    private UserService userService;

    @Resource
    private List<UserInfoService> userInfoServices;

    @PostMapping("/login")
    public ResultVo<?> login(@RequestBody LoginReq loginReq, HttpServletResponse response) {

        LoginMethod method = loginReq.getMethod();

        LoginMethodHandler handler = Optional.ofNullable(LoginMethodFactory.getHandler(method)).orElseThrow(() -> new BusinessException("Login method miss match: " + method));

        String token = handler.doLogin(loginReq);

        return ResultVo.asSuccess(Collections.singletonMap("token", token));
    }


    @PostMapping("/deleteAccount")
    public ResultVo<?> deleteAccount(TokenUser requestUserInfo) {
        userService.deleteAccount(requestUserInfo.getUid());
        return ResultVo.asSuccess();
    }

    @GetMapping("/userInfo")
    @Cacheable(cacheNames = "user:info:", key = "#requestUserInfo.uid")
    public ResultVo<?> getUserInfo(TokenUser requestUserInfo) {
        UserAddr ret = new UserAddr();
        for (UserInfoService service : userInfoServices) {
            UserAddr userInfo = service.getUserInfo(requestUserInfo.getUid());
            if (Objects.isNull(userInfo)) {
                log.warn("instance: {} get user info fail. uid: {}", service.getClass().getSimpleName(), requestUserInfo.getUid());
                continue;
            }
            BeanUtils.copyProperties(userInfo, ret);
        }
        ret.setPassword(null);
        ret.setUid(requestUserInfo.getUid());
        return ResultVo.asSuccess(ret);
    }

}
