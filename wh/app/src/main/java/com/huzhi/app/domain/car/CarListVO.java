package com.huzhi.app.domain.car;

import com.huzhi.app.domain.car.CarListBaseVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CarListVO {
    private Boolean isEnd;
    private String wp;
    private List<CarListBaseVO> list;
}
