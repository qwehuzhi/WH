package com.huzhi.console.domain.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class ClientSelectVO {
    private BigInteger id;
    private String name;
}
