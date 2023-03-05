package com.huzhi.module.module.car.service;

import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.car.mapper.CarMapper;
import com.huzhi.module.module.enterprise.entity.Enterprise;
import com.huzhi.module.module.enterprise.service.EnterpriseService;
import com.huzhi.module.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class CarService {
    private final CarMapper mapper;
    private final EnterpriseService enterpriseService;
    @Autowired
    public CarService(CarMapper mapper,
                      EnterpriseService enterpriseService){
        this.mapper=mapper;
        this.enterpriseService=enterpriseService;
    }

    /**
     * 查询：根据查询（未删除）车辆数据
     */
    public Car getById(BigInteger id){
        return mapper.getById(id);
    }

    /**
     * 查询：根据id取出车辆数据
     */
    public Car extractById(BigInteger id){
        return mapper.extractById(id);
    }
    /**
     * 插入车辆信息
     * @return
     */
    public BigInteger insert(String numberPlate,String model,Integer businessStatus,String licenseFrontPic,
                          String licenseBackPic,String transportPic,String trailerPic,String transport,
                          String trailer,BigInteger enterpriseId){
        Car insertCar=new Car();
        insertCar.setNumberPlate(numberPlate);
        insertCar.setModel(model);
        insertCar.setBusinessStatus(businessStatus);
        insertCar.setLicenseFrontPic(licenseFrontPic);
        insertCar.setLicenseBackPic(licenseBackPic);
        insertCar.setTransportPic(transportPic);
        insertCar.setTrailerPic(trailerPic);
        insertCar.setTransport(transport);
        insertCar.setTrailer(trailer);
        insertCar.setCarEnterpriseId(enterpriseId);
        insertCar.setCreateTime(TimeUtil.getNowTime());
        mapper.insert(insertCar);
        return insertCar.getId();
    }
    /**
     * 修改车辆信息
     * @return
     */
    public BigInteger update(BigInteger id,String numberPlate,String model,Integer businessStatus,Integer examineStatus,
                          String licenseFrontPic,String licenseBackPic,String transportPic,String trailerPic,
                          String transport,String trailer,BigInteger enterpriseId,Integer isDeleted){
        Car updateCar=new Car();
        updateCar.setId(id);
        updateCar.setExamineStatus(examineStatus);
        updateCar.setNumberPlate(numberPlate);
        updateCar.setModel(model);
        updateCar.setBusinessStatus(businessStatus);
        updateCar.setLicenseFrontPic(licenseFrontPic);
        updateCar.setLicenseBackPic(licenseBackPic);
        updateCar.setTransportPic(transportPic);
        updateCar.setTrailerPic(trailerPic);
        updateCar.setTransport(transport);
        updateCar.setTrailer(trailer);
        updateCar.setCarEnterpriseId(enterpriseId);
        updateCar.setIsDeleted(isDeleted);
        mapper.update(updateCar);
        return updateCar.getId();
    }
    /**
     *
     * 删除：逻辑删除
     */
    public int delete(BigInteger id){
        return mapper.delete(id,TimeUtil.getNowTime());
    }
    /**
     * 查询：列表显示所有车辆信息并分页
     */


    public List<Car> getCarList(String numberPlate, String enterpriseName, int page, int pageSize){
        String enterpriseId=enterpriseService.getIdByOption(enterpriseName);
        return mapper.getCarList(numberPlate,enterpriseId,(page-1)*pageSize,pageSize);
    }
    /**
     * 查询：车辆信息总数附带模糊查询
     */
    public Integer getCarsTotalForApp(String numberPlate, String enterpriseName){
        String enterpriseId=enterpriseService.getIdByOption(enterpriseName);
        return mapper.getCarsTotal(numberPlate,enterpriseId);
    }

    /**
     * 增加，修改：两个方法的结合
     * 先进行一个一个的校验，校验成功的内容插入，通过是否传入id判断操作为修改还是新增，再相对进行不同的操作
     * 最后返回值都是id
     */

    public BigInteger editCar(BigInteger id, String numberPlate, String model, Integer businessStatus, Integer examineStatus,
                              String licenseFrontPic, String licenseBackPic, String transportPic, String trailerPic,
                              String transport, String trailer, Integer isDeleted, BigInteger enterpriseId){
        if (numberPlate==null || numberPlate.equals("")){
            throw new RuntimeException("Error not have numberPlate");
        }
        if (model==null || model.equals("")){
            throw new RuntimeException("Error not have model");
        }
        if (transport==null || transport.equals("")){
            throw new RuntimeException("Error not have transport");
        }
        if (licenseFrontPic==null ){
            throw new RuntimeException("Error not have licenseFrontPic");
        }
        if (licenseBackPic==null ){
            throw new RuntimeException("Error not have licenseBackPic");
        }
        if (transportPic==null ){
            throw new RuntimeException("Error not have transportPic");
        }
        if (enterpriseId==null){
            throw new RuntimeException("Error not have enterpriseId");
        }
        Enterprise enterprise=enterpriseService.getById(enterpriseId);
        if(enterprise==null){
            throw new RuntimeException("Error not have enterprise");
        }
        if (id != null &&isDeleted==null){
            throw new RuntimeException("Error not have isDeleted");
        }
        Car oldCar = mapper.extractById(id);
        if (id != null && oldCar ==null){
            throw new RuntimeException("Database not have ID:"+id);
        }
        Car car=new Car();
        car.setNumberPlate(numberPlate);
        car.setModel(model);
        car.setTransport(trailer);
        car.setTrailerPic(trailerPic);
        car.setLicenseFrontPic(licenseFrontPic);
        car.setLicenseBackPic(licenseBackPic);
        car.setTransportPic(transportPic);
        car.setBusinessStatus(businessStatus);
        car.setCarEnterpriseId(enterpriseId);
        if (id != null) {
            car.setExamineStatus(examineStatus);
            car.setIsDeleted(isDeleted);
            car.setId(id);
            Integer number=mapper.update(car);
            if (number != 1){
                throw new RuntimeException("Update is fail");
            }
        }
        else{
            car.setCreateTime(TimeUtil.getNowTime());
            car.setIsDeleted(isDeleted);
            Integer number=mapper.insert(car);
            if (number == null){
                throw new RuntimeException("Insert is fail");
            }
        }
        return car.getId();
    }

}
