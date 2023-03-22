package com.huzhi.module.module.statistics.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 按天统计的数据表
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Data
@Accessors(chain = true)
public class Statistics{
    private BigInteger id;

    /**
     * 运单单日增量
     */
    private BigInteger waybillIncrement;

    /**
     * 运单总量
     */
    private BigInteger waybillTotal;

    /**
     * 客户单日增量
     */
    private BigInteger clientIncrement;

    /**
     * 客户总量
     */
    private BigInteger clientTotal;

    /**
     * 车辆单日增量
     */
    private BigInteger carIncrement;

    /**
     * 车辆总量
     */
    private BigInteger carTotal;

    /**
     * 司机单日增量
     */
    private BigInteger driverIncrement;

    /**
     * 司机总量
     */
    private BigInteger driverTotal;

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

    private Integer createTime;

    private Integer updateTime=(int) (new Date().getTime()/1000);

    private Integer isDeleted;


}
