package com.huzhi.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain= true)
public class ImageVO {
    private String scr;
    private Double ar;
}
