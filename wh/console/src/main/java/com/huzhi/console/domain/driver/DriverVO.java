package com.huzhi.console.domain.driver;

import com.huzhi.console.domain.utils.ImageVO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DriverVO {
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
        private ImageVO idNumberFrontPic;
        /**
         * 身份证反面照片
         */
        private ImageVO idNumberBackPic;
        /**
         * 驾驶证正面照片
         */
        private ImageVO drivingLicenseFrontPic;
        /**
         * 驾驶证反面照片
         */
        private ImageVO drivingLicenseBackPic;

        /**
         * 从业资格证照片
         */
        private ImageVO qualificationCertificatePic;

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
        private String idNumberExpired;

        /**
         * 驾驶证过期时间
         */
        private String drivingLicenseExpired;

        private String createTime;

        private String updateTime;

        private Integer isDeleted;

    }
