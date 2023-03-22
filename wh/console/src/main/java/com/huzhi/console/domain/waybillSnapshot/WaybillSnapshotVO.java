package com.huzhi.console.domain.waybillSnapshot;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.Date;
@Data
@Accessors(chain = true)
public class WaybillSnapshotVO {

    /**
     * 运单号
     */
    private String waybillNumber;

    /**
     * 承运单位
     */
    private String carrier;

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
    private String  deliveryTime;

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
    private String  arrivalTime;

    /**
     * 发货省
     */
    private String arrivalProvince;

    /**
     * 发货市
     */
    private String arrivalCity;

    /**
     * 发货区
     */
    private String arrivalRegion;

    /**
     * 发货地址
     */
    private String arrivalAddress;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 车牌
     */
    private String carNumberPlate;

    /**
     * 车型
     */
    private String carModel;

    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 司机手机号码
     */
    private String driverPhone;

    /**
     * 司机身份证号
     */
    private String driverIdNumber;

    /**
     * 客户公司名称
     */
    private String clientName;

    /**
     * 客户联系号码
     */
    private String clientPhone;

    /**
     * 客户公司统一社会信用代码
     */
    private String clientCreditCode;

    /**
     * 承运公司名称
     */
    private String companyName;

    /**
     * 承运公司联系号码
     */
    private String companyPhone;

    /**
     * 承运公司统一社会信用代码
     */
    private String companyCreditCode;

    private String createTime;

    private String updateTime;
}
