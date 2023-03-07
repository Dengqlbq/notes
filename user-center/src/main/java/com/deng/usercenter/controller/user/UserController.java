package com.deng.usercenter.controller.user;
import com.deng.usercenter.auth.CheckLogin;
import com.deng.usercenter.domain.dto.user.JwtTokenResDTO;
import com.deng.usercenter.domain.dto.user.LoginResDTO;
import com.deng.usercenter.domain.dto.user.UserLoginDTO;
import com.deng.usercenter.domain.dto.user.UserResDTO;
import com.deng.usercenter.domain.entity.user.User;
import com.deng.usercenter.service.user.UserService;
import com.deng.usercenter.utils.JwtOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;
    private final JwtOperator jwtOperator;

    @GetMapping("/{id}")
    @CheckLogin
    public User findById(@PathVariable Integer id) {
        return this.userService.findById(id);
    }

    @PostMapping("/login")
    public LoginResDTO login(@RequestBody UserLoginDTO loginDTO) {
        // 微信开发者工具没有linux版本，登录功能仅做模拟
        String openId = "fake open id";

        User user = this.userService.login(loginDTO, openId);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("wxNickname", user.getWxNickname());
        userInfo.put("role", user.getRoles());

        String token = jwtOperator.generateToken(userInfo);
        log.info("为用户: {} 生成token: {}", user.getWxId(), token);

        UserResDTO userResDTO = UserResDTO.builder()
                .id(user.getId())
                .avatarUrl(user.getAvatarUrl())
                .bonus(user.getBonus())
                .wxNickname(user.getWxNickname())
                .build();

        JwtTokenResDTO jwtTokenResDTO = JwtTokenResDTO.builder()
                .token(token)
                .expireTime(jwtOperator.getExpirationTime().getTime())
                .build();

        return LoginResDTO.builder()
                .user(userResDTO)
                .token(jwtTokenResDTO)
                .build();
    }
}
