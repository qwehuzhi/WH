package com.huzhi.console.domain.driver;

import com.huzhi.console.domain.car.CarListBaseVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DriverListVO {
    private Integer total;//总数
    private Integer pageSize;//每页大小
    private List<DriverListBaseVO> list;
}
