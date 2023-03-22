package com.huzhi.console.domain.log;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class LogListBaseVO {
    private BigInteger userId;
    private Integer type;
    private String operateTime;
    private String operate;
    private String extra;
}
