package com.deng.usercenter.rocketmq;

import com.deng.usercenter.dao.bonus.BonusEventLogMapper;
import com.deng.usercenter.dao.user.UserMapper;
import com.deng.usercenter.domain.entity.bonus.BonusEventLog;
import com.deng.usercenter.domain.entity.user.User;
import com.deng.usercenter.domain.entity.user.dto.messaging.UserAddBonusMsgDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RocketMQMessageListener(consumerGroup = "consumer-group", topic = "add-bonus")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class addBonusListener implements RocketMQListener<UserAddBonusMsgDTO> {
    private final UserMapper userMapper;
    private final BonusEventLogMapper bonusEventLogMapper;
    private final String ADD_BONUS_EVENT = "CONTRIBUTE";
    private final String ADD_BONUS_EVENT_DESCRIPTION = "投稿加积分";

    @Override
    public void onMessage(UserAddBonusMsgDTO userAddBonusMsgDTO) {
        Integer userId = userAddBonusMsgDTO.getUserId();
        Integer bonus = userAddBonusMsgDTO.getBonus();

        User user = this.userMapper.selectByPrimaryKey(userId);
        user.setBonus(user.getBonus() + bonus);
        this.userMapper.updateByPrimaryKeySelective(user);
        log.info("用户 {} 增加 {} 积分", userId, bonus);

        this.bonusEventLogMapper.insert(
                BonusEventLog.builder()
                        .userId(userId)
                        .value(bonus)
                        .createTime(new Date())
                        .event(ADD_BONUS_EVENT)
                        .description(ADD_BONUS_EVENT_DESCRIPTION)
                        .build());
    }
}
