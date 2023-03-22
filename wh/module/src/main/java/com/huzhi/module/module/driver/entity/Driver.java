package com.huzhi.module.module.driver.entity;

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
 * 司机信息表
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Data
@Accessors(chain = true)
public class Driver {
    private BigInteger id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String idNumber;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 驾驶证姓名
     */
    private String drivingLicenseName;
    /**
     * 驾驶证发证机关
     */
    private String drivingLicenseAuthority;
    /**
     * 驾驶证准驾车型
     */
    private String model;
    /**
     * 身份证正面照片
     */
    private String idNumberFrontPic;
    /**
     * 身份证反面照片
     */
    private String idNumberBackPic;
    /**
     * 驾驶证正面照片
     */
    private String drivingLicenseFrontPic;
    /**
     * 驾驶证反面照片
     */
    private String drivingLicenseBackPic;

    /**
     * 从业资格证照片
     */
    private String qualificationCertificatePic;

    /**
     * 审核备注
     */
    private String examineRemarks;

    /**
     * 审核状态
     */
    private Integer examineStatus;

    /**
     * 身份证过期时间
     */
    private Integer idNumberExpired;

    /**
     * 驾驶证过期时间
     */
    private Integer drivingLicenseExpired;

    private Integer createTime;

    private Integer updateTime=(int) (new Date().getTime()/1000);

    private Integer isDeleted;


}
