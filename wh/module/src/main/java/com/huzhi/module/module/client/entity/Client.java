package com.huzhi.module.module.client.entity;

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
 * 客户公司表
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Data
@Accessors(chain = true)
public class Client{
    private BigInteger id;

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

    private Integer createTime;
    private Integer updateTime=(int) (new Date().getTime()/1000);
    private Integer isDeleted;


}
