package com.huzhi.module.module.waybill.service;


import com.alibaba.fastjson.JSON;
import com.huzhi.module.code.ExamineStatusCode;
import com.huzhi.module.code.OperateCode;
import com.huzhi.module.code.TypeCode;
import com.huzhi.module.code.WaybillExamineCode;
import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.car.service.CarService;
import com.huzhi.module.module.client.entity.Client;
import com.huzhi.module.module.client.service.ClientService;
import com.huzhi.module.module.company.entity.Company;
import com.huzhi.module.module.company.service.CompanyService;
import com.huzhi.module.module.driver.entity.Driver;
import com.huzhi.module.module.driver.service.DriverService;
import com.huzhi.module.module.enterprise.service.EnterpriseService;
import com.huzhi.module.module.log.service.LogService;
import com.huzhi.module.module.waybill.entity.Waybill;
import com.huzhi.module.module.waybillSnapshot.entity.WaybillSnapshot;
import com.huzhi.module.module.waybillSnapshot.service.WaybillSnapshotService;
import com.huzhi.module.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
public class BaseWaybillRelevanceService {
    private final WaybillService waybillService;
    private final CarService carService;
    private final DriverService driverService;
    private final ClientService clientService;
    private final CompanyService companyService;
    private final EnterpriseService enterpriseService;
    private final LogService logService;
    private final WaybillSnapshotService waybillSnapshotService;
    @Autowired
    public BaseWaybillRelevanceService(WaybillService waybillService,
                                       CarService carService,
                                       DriverService driverService,
                                       ClientService clientService,
                                       CompanyService companyService,
                                       EnterpriseService enterpriseService,
                                       LogService logService,
                                       WaybillSnapshotService waybillSnapshotService){
        this.waybillService=waybillService;
        this.carService=carService;
        this.driverService=driverService;
        this.clientService=clientService;
        this.companyService=companyService;
        this.enterpriseService=enterpriseService;
        this.logService=logService;
        this.waybillSnapshotService=waybillSnapshotService;
    }
    /**
     *  校验传入参数不能为bull的
     *  业务方面的校验
     *  备注不需要校验
     */
    private boolean unsafeCheckForWaybill(BigInteger carId,BigInteger driverId,BigInteger clientId,BigInteger companyId,
                          String typeOfGoods,Integer weightOfGoods,
                          Integer volumeOfGoods,Integer deliveryTime,String deliveryProvince,String deliveryCity,
                          String deliveryRegion,String deliveryAddress,Integer arrivalTime,String arrivalProvince,
                          String arrivalCity,String arrivalRegion,String arrivalAddress,
                          BigInteger userId, Integer time,Integer isDeleted){
        if(BaseUtil.isEmpty(typeOfGoods,weightOfGoods,volumeOfGoods,deliveryTime,deliveryProvince,deliveryCity,
                deliveryRegion,deliveryAddress,arrivalTime,arrivalProvince,arrivalCity,arrivalRegion,
                arrivalAddress,userId,time)){
            throw new RuntimeException("parameter have null");
        }
        if(isDeleted == null){
            throw new RuntimeException("parameter have null");
        }
        if(weightOfGoods > 50){
            throw new RuntimeException("overload");
        }
        if(volumeOfGoods > 140){
            throw new RuntimeException("Cargo volume exceeds");
        }
        if(arrivalTime < deliveryTime || arrivalTime.equals(deliveryTime)){
            throw new RuntimeException("The time period of the waybill is incorrect");
        }
        //检查运单关联信息是否符合要求
        //车辆需要审核通过
        Car car=carService.getById(carId);
        if(car.getExamineStatus().equals(ExamineStatusCode.CarExamineSuccess.getCode())){
            throw new RuntimeException("Vehicle audit failed");
        }
        //托运公司是否存在
        Client client = clientService.getById(clientId);
        if(client == null){
            throw new RuntimeException("The shipping company does not exist");
        }
        //托运公司和车辆所属公司名称不能一致
        if(enterpriseService.getById(car.getEnterpriseId()).getName().equals(client.getName())){
            throw new RuntimeException("The shipping company and the company to which the vehicle belongs cannot be consistent");
        }
        //司机需要审核通过
        Driver driver=driverService.getById(driverId);
        if(driver.getExamineStatus().equals(ExamineStatusCode.DriverExamineSuccess.getCode())){
            throw new RuntimeException("Driver review failed");
        }
        //承运公司需要已有
        Company company=companyService.getById(companyId);
        if(company == null){
            throw new RuntimeException("The carrier company does not exist");
        }
        //检验时间有无冲突
        if(!waybillService.timeExamine(deliveryTime,arrivalTime,carId,driverId)){
            throw new RuntimeException("The time period of the waybill is incorrect");
        }
        return true;
    }
    /**
     * 新增,修改一条运单
     */
    @Transactional(rollbackFor = Exception.class)
    public BigInteger waybillEdit(BigInteger id, BigInteger carId, BigInteger driverId, BigInteger clientId, BigInteger companyId,
                                  String typeOfGoods, Integer weightOfGoods,
                                  Integer volumeOfGoods, Integer deliveryTime, String deliveryProvince, String deliveryCity,
                                  String deliveryRegion, String deliveryAddress, Integer arrivalTime, String arrivalProvince,
                                  String arrivalCity, String arrivalRegion, String arrivalAddress, String remarks,
                                  String examineRemarks, Integer examineStatus,
                                  BigInteger userId, Integer time, Integer isDeleted){
        if(unsafeCheckForWaybill(carId,driverId,clientId,companyId,typeOfGoods,weightOfGoods,volumeOfGoods,deliveryTime,
                deliveryProvince,deliveryCity,deliveryRegion,deliveryAddress,arrivalTime,arrivalProvince,
                arrivalCity,arrivalRegion,arrivalAddress,userId,time,isDeleted)){
            //生成20位的运单号
            String waybillNumber="YH89595"+ BaseUtil.currentSeconds()+"000";
            Waybill entity=new Waybill();
            entity.setCarId(carId);
            entity.setDriverId(driverId);
            entity.setClientId(clientId);
            entity.setCompanyId(companyId);
            entity.setTypeOfGoods(typeOfGoods);
            entity.setWeightOfGoods(weightOfGoods);
            entity.setVolumeOfGoods(volumeOfGoods);
            entity.setDeliveryTime(deliveryTime);
            entity.setDeliveryProvince(deliveryProvince);
            entity.setDeliveryCity(deliveryCity);
            entity.setDeliveryRegion(deliveryRegion);
            entity.setDeliveryAddress(deliveryAddress);
            entity.setArrivalTime(arrivalTime);
            entity.setArrivalProvince(arrivalProvince);
            entity.setArrivalCity(arrivalCity);
            entity.setArrivalRegion(arrivalRegion);
            entity.setArrivalAddress(arrivalAddress);
            entity.setRemarks(remarks);
            entity.setIsDeleted(isDeleted);
            if(BaseUtil.isEmpty(id)){
                entity.setWaybillNumber(waybillNumber);
                entity.setCreateTime(BaseUtil.currentSeconds());
                Integer back = waybillService.insert(entity);
                //记录到日志表
                logService.insert(userId, TypeCode.Waybill.getId(),time, OperateCode.WaybillInsert.getCode(), JSON.toJSONString(entity));
                return BigInteger.valueOf(back);
            }else {
                entity.setId(id);
                entity.setExamineRemarks(examineRemarks);
                entity.setExamineStatus(examineStatus);
                waybillService.update(entity);
                //记录到日志表
                logService.insert(userId, TypeCode.Waybill.getId(),time,OperateCode.WaybillUpdate.getCode(), JSON.toJSONString(entity));
                return id;
            }
        }
        return null;
    }
    /**
     * 获取运单列表查询需要数据
     */
    public List<Waybill> getWaybillList(Integer page, Integer pageSize, String companyName, String clientName,
                                        String driverName, String carNumberPlate){
        if(BaseUtil.isEmpty(page) || BaseUtil.isEmpty(pageSize)){
            throw new RuntimeException("page or pageSize is null");
        }
        if(!BaseUtil.isEmpty(companyName)){
            companyName=companyService.getCompanyIdByName(companyName);
        }
        if(!BaseUtil.isEmpty(clientName)){
            clientName=clientService.getClientIdByName(clientName);
        }
        if(!BaseUtil.isEmpty(driverName)){
            driverName=driverService.getIdByOption(driverName);
        }
        if(!BaseUtil.isEmpty(carNumberPlate)){
            carNumberPlate=carService.getIdByOption(carNumberPlate);
        }
        return waybillService.getList(page,pageSize,companyName,clientName,driverName,carNumberPlate);
    }
    /**
     * 获取运单列表总数
     */
    public Integer getWaybillListTotal(String companyName, String clientName,
                                       String driverName, String carNumberPlate){
        if(!BaseUtil.isEmpty(companyName)){
            companyName=companyService.getCompanyIdByName(companyName);
        }
        if(!BaseUtil.isEmpty(clientName)){
            clientName=clientService.getClientIdByName(clientName);
        }
        if(!BaseUtil.isEmpty(driverName)){
            driverName=driverService.getIdByOption(driverName);
        }
        if(!BaseUtil.isEmpty(carNumberPlate)){
            carNumberPlate=carService.getIdByOption(carNumberPlate);
        }
        return waybillService.getListTotal(companyName,clientName,driverName,carNumberPlate);
    }
    /**
     * 审核运单
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer examine(BigInteger id,Integer examineStatus,String examineRemarks,BigInteger userId, Integer time) {
        if (BaseUtil.isEmpty(id) || BaseUtil.isEmpty(examineStatus) || BaseUtil.isEmpty(userId) || BaseUtil.isEmpty(time)) {
            throw new RuntimeException("parameter have null");
        }
        if (!(examineStatus == 1 || examineStatus == 2)) {
            throw new RuntimeException("Value is out of range");
        }
        //审核通过
        if (examineStatus.equals(WaybillExamineCode.ExamineSuccess.getId())) {
                Waybill waybill = waybillService.getById(id);
                Car car = carService.getById(waybill.getCarId());
                Driver driver = driverService.getById(waybill.getDriverId());
                Client client = clientService.getById(waybill.getClientId());
                Company company = companyService.getById(waybill.getCompanyId());
                WaybillSnapshot entity = new WaybillSnapshot();
                entity.setWaybillNumber(waybill.getWaybillNumber());
                entity.setTypeOfGoods(waybill.getTypeOfGoods());
                entity.setWeightOfGoods(waybill.getWeightOfGoods());
                entity.setVolumeOfGoods(waybill.getVolumeOfGoods());
                entity.setDeliveryTime(waybill.getDeliveryTime());
                entity.setDeliveryProvince(waybill.getDeliveryProvince());
                entity.setDeliveryCity(waybill.getDeliveryCity());
                entity.setDeliveryRegion(waybill.getDeliveryRegion());
                entity.setDeliveryAddress(waybill.getDeliveryAddress());
                entity.setArrivalTime(waybill.getArrivalTime());
                entity.setArrivalProvince(waybill.getArrivalProvince());
                entity.setArrivalCity(waybill.getArrivalCity());
                entity.setArrivalRegion(waybill.getArrivalRegion());
                entity.setArrivalAddress(waybill.getArrivalAddress());
                entity.setRemarks(waybill.getRemarks());
                entity.setCarNumberPlate(car.getNumberPlate());
                entity.setCarModel(car.getModel());
                entity.setDriverName(driver.getName());
                entity.setDriverName(driver.getPhone());
                entity.setDriverIdNumber(driver.getIdNumber());
                entity.setClientName(client.getName());
                entity.setClientPhone(client.getPhone());
                entity.setClientCreditCode(client.getCreditCode());
                entity.setCompanyName(company.getName());
                entity.setCompanyPhone(company.getPhone());
                entity.setCompanyCreditCode(company.getCreditCode());
                entity.setCreateTime(BaseUtil.currentSeconds());
                waybillSnapshotService.insert(entity);
                logService.insert(userId, TypeCode.Waybill.getId(), time, OperateCode.WaybillExamineSuccess.getCode(), id.toString());
        } else if (examineStatus.equals(WaybillExamineCode.ExamineFail.getId())) {
                logService.insert(userId, TypeCode.Waybill.getId(), time, OperateCode.WaybillExamineFail.getCode(), id.toString());
        }
        return waybillService.examine(id, examineStatus, examineRemarks);
        }
}
