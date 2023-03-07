package com.deng.usercenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResDTO {
    private Integer id;
    private String avatarUrl;
    private String wxNickname;
    private Integer bonus;
}
