package com.huzhi.console.domain.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.Date;

@Data
@Accessors(chain = true)
public class WaybillInfoVO {
    private String waybillNumber;
    private String examineRemarks;
    private Integer examineStatus;
    private String typeOfGoods;
    private Integer weightOfGoods;
    private Integer volumeOfGoods;
    private Integer deliveryTime;
    private String deliveryProvince;
    private String deliveryCity;
    private String deliveryRegion;
    private String deliveryAddress;
    private Integer arrivalTime;
    private String arrivalProvince;
    private String arrivalCity;
    private String arrivalRegion;
    private String arrivalAddress;
    private String remarks;
    private String clientName;
    private String clientPhone;
    private String companyName;
    private String companyPhone;
    private String driverName;
    private String driverPhone;
    private String carNumberPlate;
    private String createTime;
    private String updateTime;

}
