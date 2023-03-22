package com.huzhi.console.domain.driver;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
@Data
@Accessors(chain = true)
public class DriverListBaseVO {
    private BigInteger id;
    private String name;
    private String phone;
    private Integer examineStatus;
}
