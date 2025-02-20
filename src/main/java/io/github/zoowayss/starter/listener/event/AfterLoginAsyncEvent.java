package io.github.zoowayss.starter.listener.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 1/3/25 15:18
 */
@Getter
@Setter
@JsonIgnoreProperties("source")
public class AfterLoginAsyncEvent extends ApplicationEvent {

    private Long uid;

    private String ip;

    public AfterLoginAsyncEvent(Object source, Long uid, String ip) {
        super(source);
        this.uid = uid;
        this.ip = ip;
    }
}
