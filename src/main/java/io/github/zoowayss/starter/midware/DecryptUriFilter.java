package io.github.zoowayss.starter.midware;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.zoowayss.starter.domain.dto.RequestHolder;
import io.github.zoowayss.starter.utils.AESUtils;
import io.github.zoowayss.starter.utils.JsonUtils;
import io.github.zoowayss.starter.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.*;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 11/14/24 11:42
 */

@Slf4j
public class DecryptUriFilter extends OncePerRequestFilter {


    public static final Set<String> encryptAppIds = new HashSet<>(Collections.singletonList("1001"));

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String cm = "doFilterInternal@DecryptUriFilter";
        String uri = httpServletRequest.getRequestURI();
        String v = httpServletRequest.getParameter("v");
        String contentType = httpServletRequest.getContentType();
        String method = httpServletRequest.getMethod();
        String appid = httpServletRequest.getHeader("appId");

        CustomHttpServletRequestWrapper wrappedRequest = new CustomHttpServletRequestWrapper(httpServletRequest);
        // 解密header
        String headerVv = httpServletRequest.getHeader("vv");
        if (StringUtils.hasText(headerVv)) {
            String headerJson = AESUtils.decrypt(headerVv);
            Map<String, String> headerMap = JsonUtils.toBean(headerJson, HashMap.class);
            wrappedRequest.addHeaders(headerMap);
            if (headerMap.containsKey(ServletUtils.APP_ID)) {
                appid = headerMap.get(ServletUtils.APP_ID);
            }
            log.info("{} decrypt:header:{},v:{}", cm, headerJson, v);
        }
        // 不需要解密直接返回
        if (!encryptAppIds.contains(appid)) {
            log.info("{} uri:{},appID：{} appid err", cm, uri, appid);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        try {
            // 解密uri
            uri = AESUtils.decrypt(v);
            uri = URLDecoder.decode(uri, "utf-8");

            log.info("{} decrypt:uri:{},v:{}", cm, uri, v);
            RequestHolder.setApiNeedEncrypt();
            Map<String, String> params = getQueryParams(uri);
            if (!params.isEmpty()) {
                wrappedRequest.addParameterMap(params);
            }

            if (doDecryptRequest(httpServletRequest, httpServletResponse, cm, uri, contentType, method, wrappedRequest)) return;

            httpServletRequest.getRequestDispatcher("/v1/" + uri).forward(wrappedRequest, httpServletResponse);
        } finally {
            RequestHolder.apiEncryptContextRemove();
        }
    }

    private Map<String, String> getQueryParams(String url) {
        // 根据url获取参数
        Map<String, String> params = new HashMap<>();
        String[] uriOParams = StringUtils.split(url, "?");
        if (uriOParams.length == 2) {
            String[] paramsArray = StringUtils.split(uriOParams[1], "&");
            for (String param : paramsArray) {
                String[] kv = StringUtils.split(param, "=");
                params.put(kv[0], kv[1]);
            }
        }
        return params;
    }

    private boolean doDecryptRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String cm, String uri, String contentType, String method,
                                     CustomHttpServletRequestWrapper wrappedRequest) throws ServletException, IOException {

        if (isNotGetMethodAndFormRequest(method, contentType)) {
            // 获取原始参数映射
            Map<String, String[]> originalParameterMap = httpServletRequest.getParameterMap();
            // 创建一个新的参数映射，用于存放修改后的参数
            wrappedRequest.addParameterALL(originalParameterMap);
            if (originalParameterMap.containsKey("vv")) {
                String vv = originalParameterMap.get("vv")[0];
                vv = AESUtils.decrypt(vv);
                JSONObject json = JSON.parseObject(vv);

                json.forEach((key, value) -> {
                    if (value instanceof JSONArray) {
                        wrappedRequest.addParameter(key, (JSONArray) value);
                    } else wrappedRequest.addParameter(key, String.valueOf(value));
                });

                log.info("{} {} decrypt:formData:{},contentType：{}", cm, uri, vv, contentType);

                httpServletRequest.getRequestDispatcher("/v1/" + uri).forward(wrappedRequest, httpServletResponse);
                return true;
            }
        } else if (isNotGetMethodAndJsonRequest(method, contentType)) {

            String formData = httpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            // 处理 formData
            // 处理请求体
            formData = processRequestBody(formData);
            if (formData == null) {
                return false;
            }

            log.info("{} {},decrypt:formData:{},contentType：{}", cm, uri, formData, contentType);
            // 将处理后的请求体重新写入请求对象
            wrappedRequest.setFinalFormData(formData);
            httpServletRequest.getRequestDispatcher("/v1/" + uri).forward(wrappedRequest, httpServletResponse);
            return true;
        }


        return false;
    }

    private boolean isNotGetMethodAndFormRequest(String method, String contentType) {
        return !"GET".equalsIgnoreCase(method) && contentType != null && (contentType.startsWith("application/x-www-form-urlencoded") || contentType.startsWith("multipart/form-data"));
    }

    private boolean isNotGetMethodAndJsonRequest(String method, String contentType) {
        return !"GET".equalsIgnoreCase(method) && contentType != null && contentType.startsWith("application/json");
    }

    private String processRequestBody(String requestBody) {
        String v = null;
        if (StringUtils.hasText(requestBody)) {
            JSONObject json = JSON.parseObject(requestBody);
            v = AESUtils.decrypt(json.getString("v"));
        }
        return v;
    }

    static class ServletInputStreamWrapper extends ServletInputStream {

        private final ByteArrayInputStream byteArrayInputStream;

        public ServletInputStreamWrapper(ByteArrayInputStream byteArrayInputStream) {
            this.byteArrayInputStream = byteArrayInputStream;
        }

        @Override
        public boolean isFinished() {
            return byteArrayInputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() throws IOException {
            return byteArrayInputStream.read();
        }
    }

    static class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final Map<String, String[]> customParameterMap;

        private final HashMap<String, String> customHeaders;

        private String finalFormData;

        public CustomHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
            this.customParameterMap = new HashMap<>();
            this.customHeaders = new HashMap<>();
        }

        public void setFinalFormData(String finalFormData) {
            this.finalFormData = finalFormData;
        }

        public void addParameter(String name, String value) {
            customParameterMap.put(name, new String[]{value});
        }

        public void addParameter(String name, JSONArray value) {
            customParameterMap.put(name, value.toArray(new String[value.size()]));
        }

        public void addParameterALL(Map<String, String[]> valus) {
            customParameterMap.putAll(valus);
        }

        public void addParameterMap(Map<String, String> valus) {
            valus.forEach((k, v) -> {
                customParameterMap.put(k, new String[]{v});
            });

        }

        public void addHeader(String name, String value) {
            customHeaders.put(name, value);
        }

        public void addHeaders(Map<String, String> values) {
            customHeaders.putAll(values);
        }

        @Override
        public String getHeader(String name) {
            String headerValue = customHeaders.get(name);
            if (headerValue != null) {
                return headerValue;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Map<String, String> allHeaders = new HashMap<>(customHeaders);
            Enumeration<String> requestHeaderNames = super.getHeaderNames();
            while (requestHeaderNames.hasMoreElements()) {
                String headerName = requestHeaderNames.nextElement();
                allHeaders.putIfAbsent(headerName, super.getHeader(headerName));
            }
            return Collections.enumeration(allHeaders.keySet());
        }

        @Override
        public int getIntHeader(String name) {
            String headerValue = customHeaders.get(name);
            if (headerValue != null) {
                return Integer.parseInt(headerValue);
            }
            return super.getIntHeader(name);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new ServletInputStreamWrapper(new ByteArrayInputStream(finalFormData.getBytes(StandardCharsets.UTF_8)));
        }

        @Override
        public String getParameter(String name) {
            if (customParameterMap.containsKey(name)) {
                return customParameterMap.get(name)[0]; // 返回修改后的第一个值
            }
            return super.getParameter(name);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return new HashMap<>(customParameterMap); // 返回修改后的参数映射
        }

        @Override
        public Enumeration<String> getParameterNames() {
            Map<String, String[]> combinedParams = getParameterMap();
            return Collections.enumeration(combinedParams.keySet());
        }

        @Override
        public String[] getParameterValues(String name) {
            if (customParameterMap.containsKey(name)) {
                return customParameterMap.get(name);
            }
            return super.getParameterValues(name);
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(finalFormData.getBytes(StandardCharsets.UTF_8))));
        }
    }


}
