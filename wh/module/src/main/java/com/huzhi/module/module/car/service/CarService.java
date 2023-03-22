package com.huzhi.module.module.car.service;

import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.car.mapper.CarMapper;
import com.huzhi.module.module.enterprise.entity.Enterprise;
import com.huzhi.module.module.enterprise.service.EnterpriseService;
import com.huzhi.module.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

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
    public Integer insert(Car car){
        return mapper.insert(car);
    }
    /**
     * 修改车辆信息
     * @return
     */
    public Integer update(Car car){
        return mapper.update(car);
    }
    /**
     *
     * 删除：逻辑删除
     */
    public int delete(BigInteger id){
        return mapper.delete(id, BaseUtil.currentSeconds());
    }
    /**
     * 查询：列表显示所有车辆信息并分页
     */


    public List<Car> getCarList(String numberPlate, String enterpriseName, Integer page, Integer pageSize){
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

    public BigInteger editCar(BigInteger id, String numberPlate, String model,Integer examineStatus,
                              String licenseFrontPic, String licenseBackPic, String transportPic, String trailerPic,
                              String transport, String trailer, Integer isDeleted, BigInteger enterpriseId,
                              String examineRemarks,Integer licenseFrontExpired){
        if (BaseUtil.isEmpty(numberPlate) || BaseUtil.isEmpty(model) || BaseUtil.isEmpty(transport) ||
                BaseUtil.isEmpty(licenseFrontPic) || BaseUtil.isEmpty(licenseBackPic) ||
                BaseUtil.isEmpty(transportPic) || BaseUtil.isEmpty(enterpriseId) ||
                BaseUtil.isEmpty(licenseFrontExpired)){
            throw new RuntimeException("Error not Parameter does not exist");
        }
        Enterprise enterprise=enterpriseService.getById(enterpriseId);
        if(BaseUtil.isEmpty(enterprise)){
            throw new RuntimeException("Error not have enterprise");
        }
        if (!BaseUtil.isEmpty(id) && BaseUtil.isEmpty(isDeleted)){
            throw new RuntimeException("Error not have isDeleted");
        }
        Car oldCar = mapper.extractById(id);
        if (!BaseUtil.isEmpty(id) && BaseUtil.isEmpty(oldCar)){
            throw new RuntimeException("Database not have ID:"+id);
        }
        Car car=new Car();
        car.setNumberPlate(numberPlate);
        car.setModel(model);
        car.setTransport(transport);
        car.setTrailer(trailer);
        car.setTrailerPic(trailerPic);
        car.setLicenseFrontPic(licenseFrontPic);
        car.setLicenseBackPic(licenseBackPic);
        car.setTransportPic(transportPic);
        car.setExamineRemarks(examineRemarks);
        car.setLicenseFrontExpired(licenseFrontExpired);
        car.setEnterpriseId(enterpriseId);
        if (id != null) {
            car.setExamineStatus(examineStatus);
            car.setIsDeleted(isDeleted);
            car.setId(id);
            update(car);
        }
        else{
            car.setCreateTime(BaseUtil.currentSeconds());
            car.setIsDeleted(isDeleted);
            Integer number=insert(car);
            car.setId(BigInteger.valueOf(number));
        }
        return car.getId();
    }
    /**
     * 审核
     */
    public Integer examine(BigInteger id,Integer examineStatus,String examineRemarks){
        return mapper.examine(id, examineStatus, examineRemarks, BaseUtil.currentSeconds());
    }

    /**
     * 模糊查询返回id
     */
    public String getIdByOption(String carName){
            String IdsToString="";
            List<String> IdList=mapper.getIdByOption(carName);
            if (IdList.size() != 0){
                for(String s:IdList){
                    IdsToString=BaseUtil.implodeSearchParam(IdsToString,s);
                }
            }
            return IdsToString;
    }
    /**
     * 通过多个id获取
     */
    public List<Car> getListByIdList(List<BigInteger> idList){
        if(BaseUtil.isEmpty(idList)){
            throw new RuntimeException("idList is null");
        }
        String ids=idList.stream().distinct().collect(Collectors.toList()).toString().substring(1);
        StringBuilder enterpriseId=new StringBuilder(ids);
        enterpriseId.deleteCharAt(enterpriseId.length()-1);
        ids=enterpriseId.toString();
        return mapper.getNameByIds(ids);
    }
    /**
     *模糊搜索审核通过的车辆
     */
    public List<Car> getBySelect(String carNumberPlate){
        return mapper.getBySelect(carNumberPlate);
    }
    /**
     * 计算前一天的单日增量
     */
    public Integer getYesterdayIncrementForCar(){
        Integer begin=BaseUtil.getCalendar0Time();
        return mapper.getYesterdayIncrementForCar(begin,begin+86400);
    }
}
