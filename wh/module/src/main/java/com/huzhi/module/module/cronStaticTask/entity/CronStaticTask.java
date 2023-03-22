package com.huzhi.module.module.cronStaticTask.entity;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 定时任务表
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Data
@Accessors(chain = true)
public class CronStaticTask {

    private BigInteger id;

    /**
     * 任务执行主机ip
     */
    private String ip;

    /**
     * 定时执行的任务名
     */
    private Integer task;

    /**
     * 年
     */
    private String timeYear;

    /**
     * 月
     */
    private String timeMonth;

    /**
     * 日
     */
    private String timeDay;

    /**
     * 备注(用于重启任务信息记录)
     */
    private String remarks;

    /**
     * 任务运行状态-0未运行-1进行中-2成功-3失败
     */
    private Integer moveStatus;

    /**
     * 任务开始执行时间
     */
    private Integer startTime;

    /**
     * 任务执行完成时间
     */
    private Integer endTime;

    private Integer createTime;

    private Integer updateTime=(int) (new Date().getTime()/1000);


}
