package io.github.zoowayss.starter.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since  11/29/24 14:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TokenUser {

    private Long uid;

    private String did;

    private String ip;

    private String email;

    private String account;

    private String nickname;
}
