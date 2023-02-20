package com.huzhi.module.module.enterprise.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 公司信息表
 * </p>
 *@EqualsAndHashCode(callSuper = true) 注解的作用就是将其父类属性也进行比较
 * extends Model<Enterprise>
 *     @TableId(value = "id", type = IdType.AUTO)
 * @author huzhi
 * @since 2023-02-03
 */
@Data
@Accessors(chain = true)
public class Enterprise  {
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
    /**
     * @TableField(fill = FieldFill.INSERT)
     */

    private Integer createTime;
    /**
     * @TableField(fill = FieldFill.INSERT_UPDATE)
     */

    private Integer updateTime=(int) (new Date().getTime()/1000);

    private Integer isDeleted;

//    @Override
//    protected Serializable pkVal() {
//        return this.id;
//    }

}
