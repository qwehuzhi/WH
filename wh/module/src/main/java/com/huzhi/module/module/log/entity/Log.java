package com.huzhi.module.module.log.entity;

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
 * 日志表
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Data
@Accessors(chain = true)
public class Log{
    private BigInteger id;

    /**
     * 操作账号id
     */
    private BigInteger userId;

    /**
     * 日志记录对象
     */
    private Integer type;

    /**
     * 操作时间
     */
    private Integer operateTime;

    /**
     * 进行的操作
     */
    private String operate;

    /**
     * json格式额外信息
     */
    private String extra;

    private Integer createTime;

    private Integer updateTime=(int) (new Date().getTime()/1000);

    private Integer isDeleted;


}
