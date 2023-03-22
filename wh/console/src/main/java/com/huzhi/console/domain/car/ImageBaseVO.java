package com.huzhi.console.domain.car;

import com.huzhi.console.domain.utils.ImageVO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain= true)
public class ImageBaseVO {
    private ImageVO image;
}
