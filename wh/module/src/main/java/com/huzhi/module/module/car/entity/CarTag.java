package com.huzhi.module.module.car.entity;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 标签表
 * </p>
 *
 * @author huzhi
 * @since 2023-02-16
 */
@Data
@Accessors(chain =true)
public class CarTag {
    private BigInteger id;

    /**
     * 标签内容  vf
     */
    private String tag;
    private Integer createTime;
    private Integer updateTime=(int) (new Date().getTime()/1000);//修改时间
    private Integer isDeleted;
}
