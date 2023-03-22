package com.huzhi.module.module.car.service;

import com.huzhi.module.code.ExamineStatusCode;
import com.huzhi.module.code.OperateCode;
import com.huzhi.module.code.TypeCode;
import com.huzhi.module.module.driver.service.DriverService;
import com.huzhi.module.module.log.service.LogService;
import com.huzhi.module.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
public class BaseExamineService {
    private final CarService carService;
    private final LogService logService;
    private final DriverService driverService;
    @Autowired
    public BaseExamineService(CarService carService,
                              LogService logService,
                              DriverService driverService){
        this.carService=carService;
        this.logService=logService;
        this.driverService=driverService;
    }
    private boolean unsafeCheckForExamine(Integer examineStatus, BigInteger id, BigInteger userId, Integer time){
        if(BaseUtil.isEmpty(examineStatus) || BaseUtil.isEmpty(id) || BaseUtil.isEmpty(userId)
                || BaseUtil.isEmpty(time)){
            throw new RuntimeException("parameter is null");
        }
        return true;
    }
    /**
     * 车辆审核过程
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer CarExamine(Integer examineStatus, BigInteger id, String examineRemarks, BigInteger userId, Integer time){
        if(unsafeCheckForExamine(examineStatus,id,userId,time)){
            Integer back = carService.examine(id, examineStatus, examineRemarks);
            if(examineStatus.equals(ExamineStatusCode.CarExamineSuccess.getCode())){
                logService.insert(userId, TypeCode.Car.getId(), time, OperateCode.DriverExamineSuccess.getCode(), null);
            }else {
                logService.insert(userId, TypeCode.Car.getId(), time,OperateCode.DriverExamineFail.getCode(), null);
            }
            return back;
        }
        return null;
    }
    /**
     * 司机审核过程
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer DriverExamine(BigInteger id,Integer examineStatus,String examineRemarks, BigInteger userId, Integer time){
        if(unsafeCheckForExamine(examineStatus,id,userId,time)){
            Integer back = driverService.examine(id, examineStatus, examineRemarks);
            if(examineStatus.equals(ExamineStatusCode.DriverExamineSuccess.getCode())){
                logService.insert(userId, TypeCode.Driver.getId(), time,OperateCode.DriverExamineSuccess.getCode(), null);
            }else {
                logService.insert(userId, TypeCode.Driver.getId(), time,OperateCode.DriverExamineFail.getCode(), null);
            }
            return back;
        }
        return null;
    }

}
