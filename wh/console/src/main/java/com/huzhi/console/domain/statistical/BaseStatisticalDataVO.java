package com.huzhi.console.domain.statistical;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class BaseStatisticalDataVO {
    //时间节点
    private String time;
    //在某时间节点的量
    private BigInteger number;
}
