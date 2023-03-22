package com.huzhi.module.module.statistics.service;

import com.huzhi.module.module.car.service.CarService;
import com.huzhi.module.module.client.service.ClientService;
import com.huzhi.module.module.driver.service.DriverService;
import com.huzhi.module.module.statistics.entity.Statistics;
import com.huzhi.module.module.waybillSnapshot.service.WaybillSnapshotService;
import com.huzhi.module.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Map;

@Service
public class BaseStatisticsService {
    private final CarService carService;
    private final DriverService driverService;
    private final WaybillSnapshotService waybillSnapshotService;
    private final ClientService clientService;
    private final StatisticsService statisticsService;
    @Autowired
    public BaseStatisticsService(CarService carService,
                                 DriverService driverService,
                                 WaybillSnapshotService waybillSnapshotService,
                                 ClientService clientService,
                                 StatisticsService statisticsService){
        this.carService=carService;
        this.driverService=driverService;
        this.waybillSnapshotService=waybillSnapshotService;
        this.clientService=clientService;
        this.statisticsService=statisticsService;
    }

    /**
     * 统计前一天的数据并插入
     * @return
     */
    public BigInteger renovateStatistical(){
        BigInteger carIncrement= BigInteger.valueOf(carService.getYesterdayIncrementForCar());
        BigInteger driverIncrement = BigInteger.valueOf(driverService.getYesterdayIncrementForDriver());
        BigInteger waybillIncrement = BigInteger.valueOf(waybillSnapshotService.getYesterdayIncrementForWaybillSnapshot());
        BigInteger clientIncrement = BigInteger.valueOf(clientService.getYesterdayIncrementForClient());
        Map<Integer,String> time=BaseUtil.getCalendar();
        if(time.get(1).equals("12") && time.get(2).equals("31")){
            BigInteger zero=new BigInteger("0");
            return statisticsService.editForStatistics(null, waybillIncrement, zero, clientIncrement, zero,
                    carIncrement, zero, driverIncrement, zero, time.get(0),time.get(1),time.get(2), null);
        }else {
            Statistics entity = statisticsService.getByTime(time.get(0),time.get(1),time.get(2));
            BigInteger carTotal= entity.getCarTotal().add(carIncrement);
            BigInteger driverTotal= entity.getDriverTotal().add(driverIncrement);
            BigInteger waybillTotal= entity.getWaybillTotal().add(waybillIncrement);
            BigInteger clientTotal= entity.getClientTotal().add(clientIncrement);
            return statisticsService.editForStatistics(null, waybillIncrement, waybillTotal, clientIncrement, clientTotal,
                    carIncrement, carTotal, driverIncrement, driverTotal, time.get(0),time.get(1),time.get(2), null);
        }
    }
}
