package com.deng.contentcenter.service.content.impl;

import com.deng.contentcenter.dao.content.ShareMapper;
import com.deng.contentcenter.dao.messaging.RocketmqTransactionLogMapper;
import com.deng.contentcenter.domain.constant.MessageConstant;
import com.deng.contentcenter.domain.dto.content.ShareAuditDTO;
import com.deng.contentcenter.domain.dto.content.ShareDTO;
import com.deng.contentcenter.domain.dto.messaging.UserAddBonusMsgDTO;
import com.deng.contentcenter.domain.dto.user.UserDTO;
import com.deng.contentcenter.domain.entity.content.Share;
import com.deng.contentcenter.domain.entity.messaging.RocketmqTransactionLog;
import com.deng.contentcenter.domain.enums.AuditStatusEnum;
import com.deng.contentcenter.feignclient.UserCenterFeignClient;
import com.deng.contentcenter.service.content.ShareService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareServiceImpl implements ShareService {

    private final ShareMapper shareMapper;
    private final UserCenterFeignClient userCenterFeignClient;
    private final RocketMQTemplate rocketMQTemplate;
    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;
    private final Integer BONUS_PER_SHARE = 50;
    private final static String MQ_TRANSACTION_LOG_DO_AUDIT = "do audit";


    @Override
    public ShareDTO findById(Integer id) {
        Share share = this.shareMapper.selectByPrimaryKey(id);
        UserDTO userDTO = this.userCenterFeignClient.findById(share.getUserId());

        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }

    @Override
    public Share auditById(Integer id, ShareAuditDTO auditDTO) {
        Share share = this.shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("参数非法，该分享不存在！");
        }

        if (!AuditStatusEnum.NOT_YEA.getStatus().equals(share.getAuditStatus())) {
            throw new IllegalArgumentException("参数非法，该分享已审核！");
        }

        if (auditDTO.getAuditStatusEnum() == AuditStatusEnum.REJECT) {
            this.doAudit(id, auditDTO);
        } else {
            this.rocketMQTemplate.sendMessageInTransaction(MessageConstant.TOPIC_ADD_BONUS, MessageBuilder.withPayload(
                    UserAddBonusMsgDTO.builder().userId(share.getUserId()).bonus(BONUS_PER_SHARE).build())
                            .setHeader(RocketMQHeaders.TRANSACTION_ID, UUID.randomUUID().toString())
                            .setHeader(MessageConstant.HEADER_SHARE_ID, id)
                            .build(),
                    auditDTO
            );
        }

        return share;
    }

    @Transactional(rollbackFor = Exception.class)
    public void doAudit(Integer id, ShareAuditDTO auditDTO) {
        Share share = Share.builder()
                .id(id)
                .auditStatus(auditDTO.getAuditStatusEnum().getStatus())
                .reason(auditDTO.getReason())
                .build();
        this.shareMapper.updateByPrimaryKeySelective(share);
    }

    @Transactional(rollbackFor = Exception.class)
    public void doAuditWithMQLog(Integer id, ShareAuditDTO auditDTO, String transactionId) {
        this.doAudit(id, auditDTO);
        this.rocketmqTransactionLogMapper.insertSelective(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .log(MQ_TRANSACTION_LOG_DO_AUDIT)
                        .build()
        );
    }

    @Override
    public PageInfo<Share> query(String title, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Share> shares = this.shareMapper.selectByParam(title);
        return new PageInfo<>(shares);
    }
}
