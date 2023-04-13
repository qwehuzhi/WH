package com.huzhi.console.controller.car;

import com.huzhi.console.annotations.VerifiedUser;
import com.huzhi.console.domain.utils.ImageVO;
import com.huzhi.console.domain.car.*;
import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.car.service.BaseExamineService;
import com.huzhi.module.module.car.service.CarService;
import com.huzhi.module.module.enterprise.entity.Enterprise;
import com.huzhi.module.module.enterprise.service.EnterpriseService;
import com.huzhi.module.module.user.entity.User;
import com.huzhi.module.response.Response;
import com.huzhi.module.utils.BaseUtil;
import com.huzhi.module.utils.ImageUtil;
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
    @Autowired
    private CarService carService;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private BaseExamineService examineService;
    private final Integer pageSize=5;

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
                           @RequestParam(value = "enterpriseId") BigInteger enterpriseId,         //公司id
                           @RequestParam(value = "licenseFrontExpired") Integer licenseFrontExpired //行驶证过期时间
    ){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        numberPlate=numberPlate.trim();
        model=model.trim();
        transport=transport.trim();
        trailer=BaseUtil.isEmpty(trailer)?null:trailer.trim();
        try{
            carService.editCar(null,numberPlate,model,null,licenseFrontPic,
                    licenseBackPic,transportPic,trailerPic,transport,trailer,0,enterpriseId,
                    null,licenseFrontExpired);
            return new Response(1001);
        }catch (RuntimeException e){
            e.printStackTrace();
            log.info("Error carAdd:"+e.getMessage());
            return new Response(2002);
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
                              @RequestParam(value = "enterpriseId") BigInteger enterpriseId,
                              @RequestParam(value = "isDeleted" )Integer isDeleted,
                              @RequestParam(value = "examineRemarks",required = false) String examineRemarks,         //审核备注
                              @RequestParam(value = "licenseFrontExpired") Integer licenseFrontExpired //行驶证过期时间
    ){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        numberPlate=numberPlate.trim();
        model=model.trim();
        transport=transport.trim();
        trailer=BaseUtil.isEmpty(trailer)?null:trailer.trim();
        examineRemarks=BaseUtil.isEmpty(examineRemarks)?null:examineRemarks.trim();
        try{
            carService.editCar(id,numberPlate,model,examineStatus,licenseFrontPic,
                    licenseBackPic,transportPic,trailerPic,transport,trailer,isDeleted,enterpriseId,
                    examineRemarks,licenseFrontExpired);
            return new Response(1001);
        }catch (RuntimeException e){
            e.printStackTrace();
            log.info("Error carUpdate:"+e.getMessage());
            return new Response(2003);
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
        try{
            carService.delete(id);
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error carDelete:"+e.getMessage());
            return new Response(2004);
        }
    }
    /**
     * 后台车辆列表
     *测试语句：<a href="http://localhost:8081/car/list?page=1&numberPlate=nu">...</a>
     */
    @RequestMapping("/car/list")
    public Response carList(@VerifiedUser User loginUser,
                            @RequestParam(value = "page")Integer page,
                            @RequestParam(value = "numberPlate",required = false)String numberPlate,
                            @RequestParam(value = "enterpriseName",required = false) String enterpriseName
    ){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        numberPlate=BaseUtil.isEmpty(numberPlate)?null:numberPlate.trim();
        enterpriseName=BaseUtil.isEmpty(enterpriseName)?null:enterpriseName.trim();
        List<Car> cars=carService.getCarList(numberPlate,enterpriseName,page,pageSize);
        //将carList的id重组成新的list
        List<BigInteger> idList=cars.stream().map(Car::getEnterpriseId).collect(Collectors.toList());
        List<Enterprise> enterpriseList=enterpriseService.getByIdList(idList);
        //获取id和名称的键值对，方便返回名称
        Map<BigInteger,String> enterpriseMap=enterpriseList.stream().collect(Collectors.toMap(Enterprise::getId,Enterprise::getName));
        List<BigInteger> matchedId=enterpriseList.stream().map(Enterprise::getId).collect(Collectors.toList());
        List<CarListBaseVO> list=new ArrayList<>();
        for (Car e: cars) {
            if(matchedId.contains(e.getEnterpriseId())){
                CarListBaseVO entity=new CarListBaseVO();
                entity.setId(e.getId());
                entity.setExamineStatus(e.getExamineStatus());
                entity.setEnterpriseName(enterpriseMap.get(e.getEnterpriseId()));
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
        infoVO.setExamineRemarks(car.getExamineRemarks());
        infoVO.setLicenseFrontExpired(BaseUtil.timeStamp2Date(car.getLicenseFrontExpired()));
        infoVO.setExamineStatus(car.getExamineStatus());
        infoVO.setLicenseFrontPic(new ImageVO().setScr(car.getLicenseFrontPic()).setAr(licenseFrontWh[0].doubleValue()/licenseFrontWh[1].doubleValue()));
        infoVO.setLicenseBackPic(new ImageVO().setScr(car.getLicenseBackPic()).setAr(licenseBackWh[0].doubleValue()/licenseBackWh[1].doubleValue()));
        infoVO.setTransportPic(new ImageVO().setScr(car.getTransportPic()).setAr(transportWh[0].doubleValue()/transportWh[1].doubleValue()));
        infoVO.setTrailerPic(new ImageVO().setScr(car.getTrailerPic()).setAr(trailerWh[0].doubleValue()/trailerWh[1].doubleValue()));
        infoVO.setEnterpriseName(enterpriseService.getById(car.getEnterpriseId()).getName());
        infoVO.setCreateTime(BaseUtil.timeStamp2Date(car.getCreateTime()));
        infoVO.setUpdateTime(BaseUtil.timeStamp2Date(car.getUpdateTime()));
        infoVO.setIsDeleted(car.getIsDeleted()==1?"已删除":"未删除");
        return new Response(1001,infoVO);
    }
    /**
     * 车辆审核
     */
    @RequestMapping("/car/examine")
    public Response carExamine(@VerifiedUser User loginUser,
                               @RequestParam(value = "id") BigInteger id,
                               @RequestParam(value = "examineStatus") Integer examineStatus,
                               @RequestParam(value = "examineRemarks",required = false) String examineRemarks){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        examineRemarks=BaseUtil.isEmpty(examineRemarks)?null:examineRemarks.trim();
        try {
            examineService.CarExamine(examineStatus,id,examineRemarks,loginUser.getId(),BaseUtil.currentSeconds());
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error carExamine:"+e.getMessage());
            return new Response(2011);
        }
    }
}