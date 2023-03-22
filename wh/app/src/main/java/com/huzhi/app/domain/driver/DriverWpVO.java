package com.huzhi.app.domain.driver;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 专门用来给前端wp的vo
 */
@Data
@Accessors(chain = true)
public class DriverWpVO {
    /**
     * 第几页
     */
    private int page;
    /**
     * 搜索
     */
    private String name;
    /**
     * 搜索
     */
    private String phone;
}
