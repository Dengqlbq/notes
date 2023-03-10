package com.deng.contentcenter.domain.entity.messaging;

import javax.persistence.*;

import lombok.*;

/**
 * 表名：rocketmq_transaction_log
 * 表注释：RocketMQ事务日志表
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rocketmq_transaction_log")
public class RocketmqTransactionLog {
    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 事务id
     */
    @Column(name = "transaction_Id")
    private String transactionId;

    /**
     * 日志
     */
    private String log;
}