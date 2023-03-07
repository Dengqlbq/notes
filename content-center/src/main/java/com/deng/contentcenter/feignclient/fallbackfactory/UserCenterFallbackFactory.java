package com.deng.contentcenter.feignclient.fallbackfactory;

import com.deng.contentcenter.domain.dto.user.UserDTO;
import com.deng.contentcenter.feignclient.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCenterFallbackFactory implements FallbackFactory<UserCenterFeignClient> {
    @Override
    public UserCenterFeignClient create(Throwable cause) {
        return new UserCenterFeignClient() {
            @Override
            public UserDTO findById(Integer id) {
                log.warn("远程调用被限流或降级", cause);
                UserDTO userDTO = new UserDTO();
                userDTO.setWxNickname("调用异常默认用户");
                return userDTO;
            }
        };
    }
}
