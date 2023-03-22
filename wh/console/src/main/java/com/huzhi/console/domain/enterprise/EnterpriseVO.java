package com.huzhi.console.domain.enterprise;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain= true)
public class EnterpriseVO {
    /**
     * 公司名称
     */
    private String name;

    /**
     * 法人名称
     */
    private String legalPersonName;

    /**
     * 客户联系号码
     */
    private String phone;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 营业执照
     */
    private String businessLicensePic;

    /**
     * 法人身份证正面照片
     */
    private String legalPersonIdFrontPic;

    /**
     * 法人身份证反面照片
     */
    private String legalPersonIdReversePic;

    private String  createTime;
    private String updateTime;
}
