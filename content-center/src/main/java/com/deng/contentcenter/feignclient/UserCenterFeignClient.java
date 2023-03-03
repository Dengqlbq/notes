package com.deng.contentcenter.feignclient;

import com.deng.contentcenter.domain.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-center")
public interface UserCenterFeignClient {

    @GetMapping("/user/{id}")
    UserDTO findById(@PathVariable Integer id);
}
