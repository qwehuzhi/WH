package com.huzhi.app.domain.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class UserSignVO {
    //用户名称
    private String accountNumber;
    //用户id
    private BigInteger userId;
    //登录时创建的时间戳
    //private Integer creatTime= BaseUtil.currentSeconds();
    //加的盐
    private String name="adfg";
}
