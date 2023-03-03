package com.deng.contentcenter.service.content.impl;

import com.deng.contentcenter.dao.content.ShareMapper;
import com.deng.contentcenter.domain.dto.share.ShareDTO;
import com.deng.contentcenter.domain.dto.user.UserDTO;
import com.deng.contentcenter.domain.entity.content.Share;
import com.deng.contentcenter.feignclient.UserCenterFeignClient;
import com.deng.contentcenter.service.content.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareServiceImpl implements ShareService {

    private final ShareMapper shareMapper;
    private final UserCenterFeignClient userCenterFeignClient;

    @Override
    public ShareDTO findById(Integer id) {
        Share share = this.shareMapper.selectByPrimaryKey(id);
        UserDTO userDTO = this.userCenterFeignClient.findById(share.getUserId());

        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }
}
