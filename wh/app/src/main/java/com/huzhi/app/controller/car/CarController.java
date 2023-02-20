package com.huzhi.app.controller.car;

import com.alibaba.fastjson.JSONObject;
import com.huzhi.app.domain.car.*;
import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.car.service.BaseCarAndTagService;
import com.huzhi.module.module.car.service.CarService;
import com.huzhi.module.module.enterprise.entity.Enterprise;
import com.huzhi.module.module.enterprise.service.EnterpriseService;
import com.huzhi.module.module.user.service.BaseUserService;
import com.huzhi.module.response.Response;
import com.huzhi.module.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@RestController
public class CarController {
    /**
     * size规定分页的大小
     */
    int pageSize =5;
    private final CarService carService;
    private final EnterpriseService enterpriseService;

    private final BaseUserService baseUserService;
    private final BaseCarAndTagService baseCarAndTagService;
    @Autowired
    public CarController(CarService carService,
                         EnterpriseService enterpriseService,
                         BaseUserService baseUserService,
                         BaseCarAndTagService baseCarAndTagService){
        this.carService=carService;
        this.enterpriseService=enterpriseService;
        this.baseUserService=baseUserService;
        this.baseCarAndTagService=baseCarAndTagService;
    }
    /**
     * app车辆列表
     * 思路：获取两个类对象后开始赋值，由于是list类型（二维数组），使用foreach循环把获取到的值赋值
     * 注意：值是给对象的，二维数组内有多个对象有值，所以是先给对象值，再把对象送进数组
     * continue是因为这里获取到的对象可能是空的，controller获取对象时要注意npe，null.getName就会npe
     */
    @RequestMapping("/car/list")
    public Response carList(@RequestParam(value = "wp",required = false) String wp,
                            @RequestParam(value = "numberPlate",required = false) String numberPlate,
                            @RequestParam(value = "enterpriseName",required = false) String enterpriseName,
                            @RequestParam(value = "tag",required = false) String tag
    ) {
        WrapperPageVO wpMassage=new WrapperPageVO();
        if (wp == null || wp.equals("")){
            numberPlate=numberPlate==null?null:numberPlate.trim();
            enterpriseName=enterpriseName==null?null:enterpriseName.trim();
            wpMassage.setPage(1);
            wpMassage.setNumberPlate(numberPlate);
            wpMassage.setEnterpriseName(enterpriseName);
        }else {
            wpMassage=JSONObject.parseObject(Base64.getDecoder().decode(wp.getBytes(StandardCharsets.UTF_8)), WrapperPageVO.class);
            wpMassage.setPage(wpMassage.getPage()+1);
        }
        String wpBase= Base64.getUrlEncoder().encodeToString(JSONObject.toJSONString(wpMassage).getBytes(StandardCharsets.UTF_8));
        List<Car> cars=baseCarAndTagService.baseGetCarList(wpMassage.getNumberPlate(),wpMassage.getEnterpriseName(),tag,wpMassage.getPage(), pageSize);
        //将carList的id重组成新的list
        List<BigInteger> idList=cars.stream().map(Car::getEnterpriseId).collect(Collectors.toList());
        List<Enterprise> enterpriseList=enterpriseService.getByIdList(idList);
        //获取id和名称的键值对，方便返回名称
        Map<BigInteger,String> enterpriseMap=enterpriseList.stream().collect(Collectors.toMap(Enterprise::getId,Enterprise::getName));
        List<BigInteger> matchedId=enterpriseList.stream().map(Enterprise::getId).collect(Collectors.toList());
        List<CarListBaseVO> list=new ArrayList<>();
        for (Car e : cars) {
            if (matchedId.contains(e.getEnterpriseId())) {
                CarListBaseVO entity = new CarListBaseVO();
                entity.setId(e.getId());
                entity.setModel(e.getModel());
                entity.setNumberPlate(e.getNumberPlate());
                entity.setEnterpriseName(enterpriseMap.get(e.getEnterpriseId()));
                entity.setExamineStatus(e.getExamineStatus());
                entity.setBusinessStatus(e.getBusinessStatus());
                list.add(entity);
            }
        }
        CarListVO carListVO=new CarListVO();
        carListVO.setList(list);
        carListVO.setWp(wpBase);
        carListVO.setIsEnd(cars.size() < pageSize);
        return new Response<>(1001,carListVO);
    }
    /**
     * app车辆详情
     * 单纯的使用getset比较麻烦，但是性能比较好
     * 这里的npe不考虑在于这里能获取的id应该在列表处筛选过
     */
    @RequestMapping("/car/info")
    public Response carInfo(@RequestParam(value = "id") BigInteger id){
        Car car=carService.extractById(id);
        Integer[] licenseFrontWh=ImageUtil.getImageWidthAndHeight(car.getLicenseFrontPic());
        Integer[] licenseBackWh=ImageUtil.getImageWidthAndHeight(car.getLicenseBackPic());
        Integer[] transportWh=ImageUtil.getImageWidthAndHeight(car.getTransportPic());
        Integer[] trailerWh=ImageUtil.getImageWidthAndHeight(car.getTrailerPic());
        CarInfoVO infoVO=new CarInfoVO();
        infoVO.setNumberPlate(car.getNumberPlate());
        infoVO.setModel(car.getModel());
        infoVO.setBusinessStatus(car.getBusinessStatus());
        infoVO.setExamineStatus(car.getExamineStatus());
        infoVO.setLicenseFrontPic(new imageVO().setScr(car.getLicenseFrontPic()).setAr(licenseFrontWh[0].doubleValue()/licenseFrontWh[1].doubleValue()));
        infoVO.setLicenseBackPic(new imageVO().setScr(car.getLicenseBackPic()).setAr(licenseBackWh[0].doubleValue()/licenseBackWh[1].doubleValue()));
        infoVO.setTransportPic(new imageVO().setScr(car.getTransportPic()).setAr(transportWh[0].doubleValue()/transportWh[1].doubleValue()));
        infoVO.setTrailerPic(new imageVO().setScr(car.getTrailerPic()).setAr(trailerWh[0].doubleValue()/trailerWh[1].doubleValue()));
        infoVO.setTransport(car.getTransport());
        infoVO.setTrailer(car.getTrailer());
        infoVO.setEnterpriseName(enterpriseService.getById(car.getEnterpriseId()).getName());
    return new Response<>(1001,infoVO);
    }
}
