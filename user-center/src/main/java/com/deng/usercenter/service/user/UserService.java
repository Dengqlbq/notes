package com.deng.usercenter.service.user;

import com.deng.usercenter.domain.dto.user.UserLoginDTO;
import com.deng.usercenter.domain.entity.user.User;

public interface UserService {

    User findById(Integer id);

    User login(UserLoginDTO loginDTO, String openId);
}
