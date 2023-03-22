package com.huzhi.app.domain.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain= true)
public class WaybillListVO {
    private Boolean isEnd;
    private String wp;
    private List<WaybillListBaseVO> list;
}
