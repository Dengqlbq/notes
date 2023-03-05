package com.deng.contentcenter.service.content.impl;

import com.deng.contentcenter.dao.content.ShareMapper;
import com.deng.contentcenter.domain.dto.content.ShareAuditDTO;
import com.deng.contentcenter.domain.dto.content.ShareDTO;
import com.deng.contentcenter.domain.dto.messaging.UserAddBonusMsgDTO;
import com.deng.contentcenter.domain.dto.user.UserDTO;
import com.deng.contentcenter.domain.entity.content.Share;
import com.deng.contentcenter.domain.enums.AuditStatusEnum;
import com.deng.contentcenter.feignclient.UserCenterFeignClient;
import com.deng.contentcenter.service.content.ShareService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareServiceImpl implements ShareService {

    private final ShareMapper shareMapper;
    private final UserCenterFeignClient userCenterFeignClient;
    private final RocketMQTemplate rocketMQTemplate;
    private final String ADD_BONUS_TOPIC = "add-bonus";
    private final Integer BONUS_PER_SHARE = 50;

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

        share.setAuditStatus(auditDTO.getAuditStatusEnum().getStatus());
        this.shareMapper.updateByPrimaryKey(share);

        this.rocketMQTemplate.convertAndSend(ADD_BONUS_TOPIC,
                UserAddBonusMsgDTO.builder().userId(share.getUserId()).bonus(BONUS_PER_SHARE).build());

        return share;
    }
}
