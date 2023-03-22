package com.huzhi.console.domain.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors
public class CompanySelectVO {
    private BigInteger id;
    private String name;
}
