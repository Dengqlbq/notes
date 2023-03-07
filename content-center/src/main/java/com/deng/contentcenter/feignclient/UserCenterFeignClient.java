package com.deng.contentcenter.feignclient;

import com.deng.contentcenter.domain.dto.user.UserDTO;
import com.deng.contentcenter.feignclient.fallbackfactory.UserCenterFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-center", fallbackFactory = UserCenterFallbackFactory.class)
public interface UserCenterFeignClient {

    @GetMapping("/user/{id}")
    UserDTO findById(@PathVariable Integer id);
}
