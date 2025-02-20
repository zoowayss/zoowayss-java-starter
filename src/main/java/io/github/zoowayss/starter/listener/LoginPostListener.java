package io.github.zoowayss.starter.listener;

import io.github.zoowayss.starter.listener.event.AfterLoginAsyncEvent;
import io.github.zoowayss.starter.service.LoginPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 1/3/25 15:16
 */
@Slf4j
@Async
@Component
public class LoginPostListener {

    @Resource
    private List<LoginPostProcessor> loginPostProcessor;

    @EventListener
    public void onLogin(AfterLoginAsyncEvent event) {
        String cm = "onLogin@LoginPostListener";
        for (LoginPostProcessor processor : loginPostProcessor) {
            processor.process(event);
        }
        log.info("{} user: {} login success", cm, event.getUid());
    }
}
