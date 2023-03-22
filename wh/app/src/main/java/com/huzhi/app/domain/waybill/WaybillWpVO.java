package com.huzhi.app.domain.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 专门用来给前端wp的vo
 */
@Data
@Accessors(chain = true)
public class WaybillWpVO {
    /**
     * 第几页
     */
    private int page;
    /**
     * 搜索
     */
    private String driverName;
    /**
     * 搜索
     */
    private String carNumberPlate;
}
