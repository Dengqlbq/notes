package com.deng.contentcenter.dao.content;

import com.deng.contentcenter.domain.entity.content.Share;
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
class ShareMapperTest {

    @Autowired
    private ShareMapper shareMapper;

    private int testId;
    private String testTitle = "junit";

    @BeforeAll
    void prepareData() {
        Share share = new Share();
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        share.setTitle(this.testTitle);
        this.shareMapper.insertSelective(share);
        this.testId = share.getId();
    }

    @AfterAll
    void clear() {
        this.shareMapper.deleteByPrimaryKey(this.testId);
    }

    @Test
    void testFindById() {
        Share share = this.shareMapper.selectByPrimaryKey(this.testId);
        assertEquals(this.testTitle, share.getTitle());
    }
}