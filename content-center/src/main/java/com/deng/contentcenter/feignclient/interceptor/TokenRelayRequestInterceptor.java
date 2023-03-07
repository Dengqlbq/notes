package com.deng.contentcenter.feignclient.interceptor;

import com.deng.contentcenter.domain.constant.HttpConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class TokenRelayRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();

        String token = request.getHeader(HttpConstant.HEADER_X_TOKEN);
        if (StringUtils.isNotBlank(token)) {
            requestTemplate.header(HttpConstant.HEADER_X_TOKEN, token);
        }
    }
}
