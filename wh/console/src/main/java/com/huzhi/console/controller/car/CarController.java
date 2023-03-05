package com.huzhi.console.controller.car;

import com.huzhi.console.annotations.VerifiedUser;
import com.huzhi.console.domain.car.*;
import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.car.service.CarService;
import com.huzhi.module.module.enterprise.entity.Enterprise;
import com.huzhi.module.module.enterprise.service.EnterpriseService;
import com.huzhi.module.module.user.entity.User;
import com.huzhi.module.response.Response;
import com.huzhi.module.utils.BaseUtil;
import com.huzhi.module.utils.ImageUtil;
import com.huzhi.module.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@RestController
public class CarController {
    private final CarService carService;
    private final EnterpriseService enterpriseService;
    @Autowired
    public CarController(CarService carService,
                         EnterpriseService enterpriseService){
        this.carService=carService;
        this.enterpriseService=enterpriseService;
    }

    /**
     *后台新增车辆信息
     *测试语句：<a href="http://localhost:8081/car/add?numberPlate=浙b7 &model=A&transport=123&trailer=gua&licenseFrontPic=tu&licenseBackPic=tu&transportPic=tu&trailerPic=tu&businessStatus=1">...</a>
     */
    @RequestMapping("/car/add")
    public Response carAdd(@VerifiedUser User loginUser,
                           @RequestParam(value = "numberPlate") String numberPlate,               //车牌
                           @RequestParam(value = "model") String model,                           //车型????有问题，这个应该是下拉框
                           @RequestParam(value = "transport") String transport,                   //运输证号
                           @RequestParam(value = "trailer",required = false) String trailer,      //挂车牌号（可以不传）
                           @RequestParam(value = "licenseFrontPic") String licenseFrontPic,       //行驶证正面
                           @RequestParam(value = "licenseBackPic") String licenseBackPic,         //行驶证反面
                           @RequestParam(value = "transportPic") String transportPic,             //运输证照片
                           @RequestParam(value = "trailerPic",required = false) String trailerPic,//挂车行驶证照片（可以不传）
                           @RequestParam(value = "businessStatus") Integer businessStatus,        //业务状态
                           @RequestParam(value = "enterpriseId") BigInteger enterpriseId         //公司id
    ){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        String message;
        numberPlate=numberPlate.trim();
        model=model.trim();
        transport=transport.trim();
        trailer=trailer==null ?null:trailer.trim();
        try{
            BigInteger add=carService.editCar(null,numberPlate,model,businessStatus,null,licenseFrontPic,
                    licenseBackPic,transportPic,trailerPic,transport,trailer,0,enterpriseId);

            message="insert success id:"+add.toString();
            return new Response(1001,message);
        }catch (RuntimeException e){
            message=e.getMessage();
            e.printStackTrace();
            return new Response(1003);
        }

    }
    /**
     * 后台修改车辆信息
     * 测试语句：
     * <a href="http://localhost:8081/car/update?id=7&numberPlate=ce&model=&transport=123&trailer=gua&licenseFrontPic=tu&licenseBackPic=tu&transportPic=tu&trailerPic=tu&businessStatus=1&examineStatus=0">...</a>
     */
    @RequestMapping("/car/update")
    public Response carUpdate(@VerifiedUser User loginUser,
                              @RequestParam(value = "id") BigInteger id,
                              @RequestParam(value = "examineStatus") Integer examineStatus,       //审核状态
                              @RequestParam(value = "numberPlate") String numberPlate,
                              @RequestParam(value = "model") String model,
                              @RequestParam(value = "transport") String transport,
                              @RequestParam(value = "trailer",required = false) String trailer,
                              @RequestParam(value = "licenseFrontPic") String licenseFrontPic,
                              @RequestParam(value = "licenseBackPic") String licenseBackPic,
                              @RequestParam(value = "transportPic") String transportPic,
                              @RequestParam(value = "trailerPic",required = false) String trailerPic,
                              @RequestParam(value = "businessStatus") Integer businessStatus,
                              @RequestParam(value = "enterpriseId") BigInteger enterpriseId,
                              @RequestParam(value = "isDeleted" )Integer isDeleted
    ){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        String message;
        numberPlate=numberPlate.trim();
        model=model.trim();
        transport=transport.trim();
        trailer=trailer==null ?null:trailer.trim();
        try{
            BigInteger update= carService.editCar(id,numberPlate,model,businessStatus,examineStatus,licenseFrontPic,
                    licenseBackPic,transportPic,trailerPic,transport,trailer,isDeleted,enterpriseId);
            message="update success id:"+update.toString();
            return new Response(1001);
        }catch (RuntimeException e){
            message=e.getMessage();
            e.printStackTrace();
            return new Response(1003);
        }
    }
    /**
     * 后台删除车辆信息
     * 测试语句：<a href="http://localhost:8081/car/delete?id=8">...</a>
     */
    @RequestMapping("/car/delete")
    public Response carDelete(@VerifiedUser User loginUser,
                              @RequestParam(value = "id") BigInteger id){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        int rem=carService.delete(id);
        return rem==1? new Response(1001): new Response(1003);
    }
    /**
     * 后台车辆列表
     *测试语句：<a href="http://localhost:8081/car/list?page=1&numberPlate=nu">...</a>
     */
    @RequestMapping("/car/list")
    public Response carList(@VerifiedUser User loginUser,
                            @RequestParam(value = "page")int page,
                            @RequestParam(value = "numberPlate",required = false)String numberPlate,
                            @RequestParam(value = "enterpriseName",required = false) String enterpriseName
    ){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        int pageSize=5;
        numberPlate=numberPlate==null?null:numberPlate.trim();
        enterpriseName=enterpriseName==null?null:enterpriseName.trim();
        List<Car> cars=carService.getCarList(numberPlate,enterpriseName,page,pageSize);
        //将carList的id重组成新的list
        List<BigInteger> idList=cars.stream().map(Car::getCarEnterpriseId).collect(Collectors.toList());
        List<Enterprise> enterpriseList=enterpriseService.getByIdList(idList);
        //获取id和名称的键值对，方便返回名称
        Map<BigInteger,String> enterpriseMap=enterpriseList.stream().collect(Collectors.toMap(Enterprise::getId,Enterprise::getName));
        List<BigInteger> matchedId=enterpriseList.stream().map(Enterprise::getId).collect(Collectors.toList());
        List<CarListBaseVO> list=new ArrayList<>();
        for (Car e: cars) {
            if(matchedId.contains(e.getCarEnterpriseId())){
                CarListBaseVO entity=new CarListBaseVO();
                entity.setId(e.getId());
                entity.setExamineStatus(e.getExamineStatus());
                entity.setEnterpriseName(enterpriseMap.get(e.getCarEnterpriseId()));
                entity.setBusinessStatus(e.getBusinessStatus());
                entity.setNumberPlate(e.getNumberPlate());
                entity.setIsDeleted(e.getIsDeleted()==1?"已删除":"未删除");
                list.add(entity);
            }
        }
        CarListVO carListVO=new CarListVO();
        carListVO.setList(list);
        Integer carsTotal=carService.getCarsTotalForApp(numberPlate,enterpriseName);
        carListVO.setTotal(carsTotal);
        carListVO.setPageSize(pageSize);
        return new Response(1001,carListVO);
    }
    /**
     *后天车辆信息详情
     *测试语句：<a href="http://localhost:8081/car/info?carId=2">...</a>
     */
    @RequestMapping("/car/info")
    public Response carInfo(@VerifiedUser User loginUser,
                            @RequestParam(value = "id") BigInteger id){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        Car car=carService.extractById(id);
        Integer[] licenseFrontWh= ImageUtil.getImageWidthAndHeight(car.getLicenseFrontPic());
        Integer[] licenseBackWh=ImageUtil.getImageWidthAndHeight(car.getLicenseBackPic());
        Integer[] transportWh=ImageUtil.getImageWidthAndHeight(car.getTransportPic());
        Integer[] trailerWh=ImageUtil.getImageWidthAndHeight(car.getTrailerPic());
        CarInfoVO infoVO=new CarInfoVO();
        infoVO.setId(car.getId());
        infoVO.setNumberPlate(car.getNumberPlate());
        infoVO.setModel(car.getModel());
        infoVO.setTransport(car.getTransport());
        infoVO.setTrailer(car.getTrailer());
        infoVO.setBusinessStatus(car.getBusinessStatus());
        infoVO.setExamineStatus(car.getExamineStatus());
        infoVO.setLicenseFrontPic(new imageVO().setScr(car.getLicenseFrontPic()).setAr(licenseFrontWh[0].doubleValue()/licenseFrontWh[1].doubleValue()));
        infoVO.setLicenseBackPic(new imageVO().setScr(car.getLicenseBackPic()).setAr(licenseBackWh[0].doubleValue()/licenseBackWh[1].doubleValue()));
        infoVO.setTransportPic(new imageVO().setScr(car.getTransportPic()).setAr(transportWh[0].doubleValue()/transportWh[1].doubleValue()));
        infoVO.setTrailerPic(new imageVO().setScr(car.getTrailerPic()).setAr(trailerWh[0].doubleValue()/trailerWh[1].doubleValue()));
        infoVO.setEnterpriseName(enterpriseService.getById(car.getCarEnterpriseId()).getName());
        infoVO.setCreateTime(TimeUtil.timeConvert(car.getCreateTime()));
        infoVO.setIsDeleted(car.getIsDeleted()==1?"已删除":"未删除");
        return new Response(1001,infoVO);
    }
}