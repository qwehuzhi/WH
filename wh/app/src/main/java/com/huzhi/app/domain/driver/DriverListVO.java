package com.huzhi.app.domain.driver;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DriverListVO {
    private Boolean isEnd;
    private String wp;
    private List<DriverListBaseVO> list;
}
