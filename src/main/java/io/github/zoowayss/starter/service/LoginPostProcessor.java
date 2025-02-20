package io.github.zoowayss.starter.service;


import io.github.zoowayss.starter.listener.event.AfterLoginAsyncEvent;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 1/22/25 09:04
 */

public interface LoginPostProcessor {

    void process(AfterLoginAsyncEvent event);
}
