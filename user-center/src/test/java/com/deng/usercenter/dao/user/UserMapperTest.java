package com.deng.usercenter.dao.user;

import com.deng.usercenter.domain.entity.user.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@ExtendWith(SpringExtension.class)
// 以下注解使得可以在非static方法上使用@BeforeAll
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private int testId;
    private String testWxId = "junit";

    @BeforeAll
    void prepareData() {
        User user = new User();
        user.setWxId(this.testWxId);
        user.setWxNickname("junit test");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.insertSelective(user);
        this.testId = user.getId();
    }

    @AfterAll
    void clear() {
        this.userMapper.deleteByPrimaryKey(this.testId);
    }

    @Test
    void testFindById() {
        User user = this.userMapper.selectByPrimaryKey(this.testId);
        assertEquals(this.testWxId, user.getWxId());
    }
}