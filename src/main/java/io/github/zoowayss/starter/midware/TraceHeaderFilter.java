package io.github.zoowayss.starter.midware;

import io.github.zoowayss.starter.domain.constants.MDCKey;
import io.github.zoowayss.starter.utils.SnowflakeIdGenerator;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.*;

public class TraceHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String tid = httpRequest.getHeader(MDCKey.TID);
            if (!StringUtils.hasText(tid)) {
                HttpServletRequest wrappedRequest = new HeaderMapRequestWrapper(httpRequest);
                tid = SnowflakeIdGenerator.generator.nextId("tid_");
                ((HeaderMapRequestWrapper) wrappedRequest).addHeader(MDCKey.TID, tid);
                MDC.put(MDCKey.TID, tid);
                chain.doFilter(wrappedRequest, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    static class HeaderMapRequestWrapper extends HttpServletRequestWrapper {

        private final Map<String, String> customHeaders = new HashMap<>();

        public HeaderMapRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        public void addHeader(String name, String value) {
            customHeaders.put(name, value);
        }

        @Override
        public String getHeader(String name) {
            String customHeaderValue = customHeaders.get(name);
            if (customHeaderValue != null) {
                return customHeaderValue;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> headerNames = Collections.list(super.getHeaderNames());
            headerNames.addAll(customHeaders.keySet());
            return Collections.enumeration(headerNames);
        }
    }
}
