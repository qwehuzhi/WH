package com.huzhi.console.domain.statistical;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DataDisplayVO {
    private List<StatisticalDataVO> dataDisplay;
}
