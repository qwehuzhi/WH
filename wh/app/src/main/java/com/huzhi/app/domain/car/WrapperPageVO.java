package com.huzhi.app.domain.car;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 专门用来给前端wp的vo
 */
@Data
@Accessors(chain = true)
public class WrapperPageVO {
    /**
     * 第几页
     */
    private int page;
    /**
     * 搜索车牌号
     */
    private String numberPlate;
    /**
     * 搜索公司名称
     */
    private String enterpriseName;
}
