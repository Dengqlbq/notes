package com.deng.contentcenter.auth;

import com.deng.contentcenter.domain.constant.HttpConstant;
import com.deng.contentcenter.exception.TokenInvalidException;
import com.deng.contentcenter.utils.JwtOperator;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckLoginAspect {

    private final JwtOperator jwtOperator;

    @Around("@annotation(com.deng.contentcenter.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();

        try {
            String token = request.getHeader(HttpConstant.HEADER_X_TOKEN);
            boolean isValid = jwtOperator.validateToken(token);
            if (!isValid) {
                throw new TokenInvalidException("Token非法！");
            }

            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new TokenInvalidException("Token非法！");
        }
    }
}
