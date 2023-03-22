package com.huzhi.console.domain.statistical;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class BaseSpecificDateVO {
    private String specificDate;
    private BigInteger number;
}
