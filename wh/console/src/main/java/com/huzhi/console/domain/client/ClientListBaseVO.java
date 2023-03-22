package com.huzhi.console.domain.client;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain= true)
public class ClientListBaseVO {
    private BigInteger id;
    private String name;
    private String phone;
    private String creditCode;
}
