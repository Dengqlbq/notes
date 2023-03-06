package com.deng.contentcenter.service.content;

import com.deng.contentcenter.domain.dto.content.ShareAuditDTO;
import com.deng.contentcenter.domain.dto.content.ShareDTO;
import com.deng.contentcenter.domain.entity.content.Share;

public interface ShareService {

    ShareDTO findById(Integer id);

    Share auditById(Integer id, ShareAuditDTO auditDTO);

    void doAuditWithMQLog(Integer id, ShareAuditDTO auditDTO, String transactionId);
}
