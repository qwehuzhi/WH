package com.huzhi.console.domain.car;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain= true)
public class imageVO {
    private String scr;
    private Double ar;
}
