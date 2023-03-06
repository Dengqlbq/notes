package com.deng.contentcenter.rocketmq;

import com.deng.contentcenter.dao.messaging.RocketmqTransactionLogMapper;
import com.deng.contentcenter.domain.constant.MessageConstant;
import com.deng.contentcenter.domain.dto.content.ShareAuditDTO;
import com.deng.contentcenter.domain.entity.messaging.RocketmqTransactionLog;
import com.deng.contentcenter.service.content.ShareService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * 一个rocketMQTemplate只能对应一种事务类型
 * https://juejin.cn/post/7133245434201374734/#heading-4
 * 暂时只有addBonus事务
 */

@RocketMQTransactionListener
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddBonusTransactionListener implements RocketMQLocalTransactionListener {
    private final ShareService shareService;
    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        MessageHeaders header = message.getHeaders();
        String transactionId = (String) header.get(RocketMQHeaders.TRANSACTION_ID);
        Integer shareId = Integer.valueOf((String) header.get(MessageConstant.HEADER_SHARE_ID));

        try {
            this.shareService.doAuditWithMQLog(shareId, (ShareAuditDTO) o, transactionId);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageHeaders header = message.getHeaders();
        String transactionId = (String) header.get(RocketMQHeaders.TRANSACTION_ID);

        RocketmqTransactionLog log = this.rocketmqTransactionLogMapper.selectOne(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .build()
        );
        return log == null ? RocketMQLocalTransactionState.ROLLBACK :RocketMQLocalTransactionState.COMMIT;
    }
}
