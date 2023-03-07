package com.deng.usercenter.service.user.impl;

import com.deng.usercenter.dao.user.UserMapper;
import com.deng.usercenter.domain.constant.UserConstant;
import com.deng.usercenter.domain.dto.user.UserLoginDTO;
import com.deng.usercenter.domain.entity.user.User;
import com.deng.usercenter.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public User findById(Integer id) {
        return this.userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User login(UserLoginDTO loginDTO, String openId) {
        User user = this.userMapper.selectOne(User.builder().wxId(openId).build());
        if (user != null) {
            return user;
        }

        user = User.builder().wxId(openId).bonus(UserConstant.BONUS_REGISTER)
                .wxNickname(loginDTO.getWxNickname())
                .createTime(new Date())
                .updateTime(new Date())
                .roles(UserConstant.ROLE_USER).build();
        this.userMapper.insertSelective(user);
        return user;
    }
}
