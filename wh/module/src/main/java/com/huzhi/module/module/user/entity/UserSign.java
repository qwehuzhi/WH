package com.huzhi.module.module.user.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
@ToString
public class UserSign {

    private BigInteger userId;
    private int expiration;//获取这个sign的时间
    private String salt;//盐

}
