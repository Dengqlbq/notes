package com.deng.contentcenter.auth;

import com.deng.contentcenter.domain.constant.HttpConstant;
import com.deng.contentcenter.domain.constant.TokenConstant;
import com.deng.contentcenter.exception.TokenInvalidException;
import com.deng.contentcenter.utils.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckAuthAspect {

    private final JwtOperator jwtOperator;

    @Around("@annotation(com.deng.contentcenter.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        checkToken(getToken());
        return joinPoint.proceed();
    }

    @Around("@annotation(com.deng.contentcenter.auth.CheckRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = getToken();
        checkToken(token);
        Claims claims = jwtOperator.getClaimsFromToken(token);
        String role = (String) claims.get(TokenConstant.FIELD_ROLE);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckRole annotation = method.getAnnotation(CheckRole.class);

        String value = annotation.value();
        if (!role.equals(value)) {
            throw new TokenInvalidException("Token非法，用户角色不对应");
        }

        return joinPoint.proceed();
    }

    private String getToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        return request.getHeader(HttpConstant.HEADER_X_TOKEN);
    }

    private void checkToken(String token) {
        try {
            boolean isValid = jwtOperator.validateToken(token);
            if (!isValid) {
                throw new TokenInvalidException("Token非法！");
            }
        } catch (Throwable throwable) {
            throw new TokenInvalidException("Token非法！");
        }
    }
}
