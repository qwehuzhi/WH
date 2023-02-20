package com.huzhi.console.domain.car;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class CarInfoVO {
    private BigInteger id;//唯一id
    private String numberPlate;//车牌
    private String model;//车型
    private String enterpriseName;//公司id
    private Integer businessStatus;//业务状态
    private Integer examineStatus;//审核状态
    private imageVO licenseFrontPic;//行驶证正面
    private imageVO licenseBackPic;//行驶证反面
    private imageVO transportPic;//运输证照片
    private imageVO trailerPic;//挂车行驶证照片
    private String transport;//运输证号
    private String trailer;//挂车牌号
    private String createTime;//创建日期
    private String updateTime;//最后修改日期
    private String isDeleted;//是否删除
}
