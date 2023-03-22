package com.huzhi.console.domain.car;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;


@Data
@Accessors(chain= true)
public class CarListBaseVO {
        private BigInteger id;
        private String numberPlate;//车牌
        private String enterpriseName;//公司名称
        private Integer examineStatus;//审核状态
        private String isDeleted;//逻辑删除

}
