package com.huzhi.console.domain.statistical;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class TotalVO {
    private BigInteger total;
}
