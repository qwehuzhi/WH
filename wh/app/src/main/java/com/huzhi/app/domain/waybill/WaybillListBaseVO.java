package com.huzhi.app.domain.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;


@Data
@Accessors(chain= true)
public class WaybillListBaseVO {
        private BigInteger id;
        private String waybillNumber;
        private Integer examineStatus;
        private String clientName;
        private String clientPhone;
        private String companyName;
        private String driverName;
        private String carNumberPlate;
        private String deliveryTime;
        private String arrivalTime;
}
