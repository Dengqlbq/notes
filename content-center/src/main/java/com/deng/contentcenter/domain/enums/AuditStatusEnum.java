package com.deng.contentcenter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuditStatusEnum {
    NOT_YEA("NOT_YEA"),
    PASS("PASS"),
    REJECT("REJECT");

    private final String status;
}
