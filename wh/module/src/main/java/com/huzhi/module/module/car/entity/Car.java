package com.huzhi.module.module.car.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Data
@Accessors(chain =true)
public class Car implements Serializable {
    private BigInteger id;//唯一id
    private String numberPlate;//车牌
    private String model;//车型
    private Integer examineStatus;//审核状态
    private String licenseFrontPic;//行驶证正面
    private String licenseBackPic;//行驶证反面
    private String transportPic;//运输证照片
    private String trailerPic;//挂车行驶证照片
    private String transport;//运输证号
    private String trailer;//挂车牌号
    private BigInteger enterpriseId;//公司id
    private Integer createTime;//插入时间=(int) (new Date().getTime()/1000)
    private Integer updateTime=(int) (new Date().getTime()/1000);//修改时间
    private Integer isDeleted;//逻辑删除
    private String examineRemarks;//审核备注
    private Integer licenseFrontExpired;//行驶证过期时间
}
