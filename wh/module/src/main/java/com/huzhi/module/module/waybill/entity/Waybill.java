package com.huzhi.module.module.waybill.entity;

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
 * 运单信息表
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Data
@Accessors(chain = true)
public class Waybill{
    private BigInteger id;

    /**
     * 车辆id
     */
    private BigInteger carId;

    /**
     * 司机id
     */
    private BigInteger driverId;

    /**
     * 托运公司id
     */
    private BigInteger clientId;

    /**
     * 承运公司id
     */
    private BigInteger companyId;

    /**
     * 运单号
     */
    private String waybillNumber;

    /**
     * 货物类型
     */
    private String typeOfGoods;

    /**
     * 货物重量
     */
    private Integer weightOfGoods;

    /**
     * 货物体积
     */
    private Integer volumeOfGoods;

    /**
     * 运单开始时间
     */
    private Integer deliveryTime;

    /**
     * 发货省
     */
    private String deliveryProvince;

    /**
     * 发货市
     */
    private String deliveryCity;

    /**
     * 发货区
     */
    private String deliveryRegion;

    /**
     * 发货地址
     */
    private String deliveryAddress;

    /**
     * 运单结束时间
     */
    private Integer arrivalTime;

    /**
     * 收货省
     */
    private String arrivalProvince;

    /**
     * 收货市
     */
    private String arrivalCity;

    /**
     * 收货区
     */
    private String arrivalRegion;

    /**
     * 收货地址
     */
    private String arrivalAddress;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 审核备注
     */
    private String examineRemarks;

    /**
     * 审核状态
     */
    private Integer examineStatus;

    private Integer createTime;

    private Integer updateTime=(int) (new Date().getTime()/1000);

    private Integer isDeleted;


}
