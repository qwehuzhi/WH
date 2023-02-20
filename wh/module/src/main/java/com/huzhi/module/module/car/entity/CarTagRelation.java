package com.huzhi.module.module.car.entity;

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
 * 车辆标签关系表
 * </p>
 *
 * @author huzhi
 * @since 2023-02-16
 */
@Data
@Accessors(chain =true)
public class CarTagRelation{

    private BigInteger id;

    /**
     * 车辆id
     */
    private BigInteger carId;

    /**
     * 标签id
     */
    private BigInteger tagId;
    private Integer createTime;
    private Integer updateTime=(int) (new Date().getTime()/1000);//修改时间
    private Integer isDeleted;

}
