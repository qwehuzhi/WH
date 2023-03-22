package com.huzhi.console.domain.enterprise;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain= true)
public class EnterpriseListBaseVO {
    private BigInteger id;
    private String name;
    private String phone;
    private String creditCode;
}
