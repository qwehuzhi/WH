package com.huzhi.console.domain.statistical;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class StatisticalDataVO {
    private String arrangeName;
    private List<BaseStatisticalDataVO> statisticalData;
}
