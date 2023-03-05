package com.deng.contentcenter.domain.dto.content;

import com.deng.contentcenter.domain.enums.AuditStatusEnum;
import lombok.Data;

@Data
public class ShareAuditDTO {

    private AuditStatusEnum auditStatusEnum;
    public String reason;
}
