package com.huzhi.app.domain.car;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class CarListBaseVO implements Serializable {
    private BigInteger Id;//唯一id
    private String numberPlate;//车牌
    private String model;//车型
    private String enterpriseName;//公司id
    private Integer businessStatus;//业务状态
    private Integer examineStatus;//审核状态

}
