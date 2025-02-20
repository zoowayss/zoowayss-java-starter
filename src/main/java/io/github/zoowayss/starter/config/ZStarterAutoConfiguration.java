package io.github.zoowayss.starter.config;

import io.github.zoowayss.starter.aspect.RedissonLockAspect;
import io.github.zoowayss.starter.midware.*;
import io.github.zoowayss.starter.properties.ZProperties;
import io.github.zoowayss.starter.service.LoginPostProcessor;
import io.github.zoowayss.starter.service.TokenService;
import io.github.zoowayss.starter.service.UserRegisterPostProcessor;
import io.github.zoowayss.starter.service.UserService;
import io.github.zoowayss.starter.service.impl.DefaultUserServiceImpl;
import io.github.zoowayss.starter.service.impl.JwtTokenServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;

@Slf4j
@Configuration
@EnableConfigurationProperties(ZProperties.class)
@ComponentScan("io.github.zoowayss.starter")
@ConditionalOnProperty(prefix = "z.starter", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ZStarterAutoConfiguration implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public TokenService tokenService() {
        return new JwtTokenServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserService userService() {
        return new DefaultUserServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(prefix = "z.starter", name = "lock", havingValue = "true")
    public RedissonLockAspect lockService() {
        return new RedissonLockAspect();
    }

    @Bean
    public MyExceptionHandler globalExceptionHandler() {
        return new MyExceptionHandler();
    }

    @Bean
    public EncryptResponseBodyAdvice encryptResponseBodyAdvice() {
        return new EncryptResponseBodyAdvice();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new CollectorInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserHandlerMethodArgumentResolver());
    }

    @Bean
    public FilterRegistrationBean<DecryptUriFilter> decryptUriFilter() {
        FilterRegistrationBean<DecryptUriFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DecryptUriFilter());
        registrationBean.addUrlPatterns("/v2/*");
        registrationBean.setOrder(-3);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<TraceHeaderFilter> traceHeaderFilter() {
        FilterRegistrationBean<TraceHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TraceHeaderFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(-2);
        return registrationBean;
    }

    @Bean
    @ConditionalOnMissingBean(LoginPostProcessor.class)
    public LoginPostProcessor defaultLoginPostProcessor() {
        return event -> {
            log.info("login post processor: {}", event.getUid());
        };
    }

    @Bean
    @ConditionalOnMissingBean(UserRegisterPostProcessor.class)
    public UserRegisterPostProcessor defaultUserRegisterPostProcessor() {
        return uid -> {
            log.info("uid: {} There is no UserRegisterPostProcessor. Please implement UserRegisterPostProcessor if needed.", uid);
            return false;
        };
    }
} 