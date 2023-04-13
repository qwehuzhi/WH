package com.huzhi.app.controller.car;

import com.alibaba.fastjson.JSONObject;
import com.huzhi.app.domain.car.*;
import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.car.service.CarService;
import com.huzhi.module.module.enterprise.entity.Enterprise;
import com.huzhi.module.module.enterprise.service.EnterpriseService;
import com.huzhi.module.response.Response;
import com.huzhi.module.utils.BaseUtil;
import com.huzhi.module.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
    private static final int pageSize =5;
    @Autowired
    private  CarService carService;
    @Autowired
    private  EnterpriseService enterpriseService;
    @Autowired
    private   RedisTemplate redisTemplate;
    /**
     * app车辆列表
     */
    @RequestMapping("/car/list")
    public Response carList(@RequestParam(value = "wp",required = false) String wp,
                            @RequestParam(value = "numberPlate",required = false) String numberPlate,
                            @RequestParam(value = "enterpriseName",required = false) String enterpriseName
    ) {
        WrapperPageVO wpMassage=new WrapperPageVO();
        List<Car> cars=new ArrayList<>();
        if (BaseUtil.isEmpty(wp)){
            numberPlate=BaseUtil.isEmpty(numberPlate)?null:numberPlate.trim();
            enterpriseName=BaseUtil.isEmpty(enterpriseName)?null:enterpriseName.trim();
            wpMassage.setPage(1);
            wpMassage.setNumberPlate(numberPlate);
            wpMassage.setEnterpriseName(enterpriseName);
        }else {
            wpMassage=JSONObject.parseObject(Base64.getDecoder().decode(wp.getBytes(StandardCharsets.UTF_8)), WrapperPageVO.class);
            wpMassage.setPage(wpMassage.getPage()+1);
            cars = (List<Car>) redisTemplate.opsForValue().get(wp);//
        }
        String wpBase= Base64.getUrlEncoder().encodeToString(JSONObject.toJSONString(wpMassage).getBytes(StandardCharsets.UTF_8));
        if(BaseUtil.isEmpty(cars)) {
            cars = carService.getCarList(wpMassage.getNumberPlate(), wpMassage.getEnterpriseName(), wpMassage.getPage(), pageSize);
            try {
                redisTemplate.opsForValue().set(wpBase, cars);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                list.add(entity);
            }
        }
        CarListVO carListVO=new CarListVO();
        carListVO.setList(list);
        carListVO.setWp(wpBase);
        carListVO.setIsEnd(cars.size() < pageSize);
        return new Response(1001,carListVO);
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
        infoVO.setExamineStatus(car.getExamineStatus());
        infoVO.setExamineRemarks(car.getExamineRemarks());
        infoVO.setLicenseFrontPic(new imageVO().setScr(car.getLicenseFrontPic()).setAr(licenseFrontWh[0].doubleValue()/licenseFrontWh[1].doubleValue()));
        infoVO.setLicenseBackPic(new imageVO().setScr(car.getLicenseBackPic()).setAr(licenseBackWh[0].doubleValue()/licenseBackWh[1].doubleValue()));
        infoVO.setTransportPic(new imageVO().setScr(car.getTransportPic()).setAr(transportWh[0].doubleValue()/transportWh[1].doubleValue()));
        infoVO.setTrailerPic(new imageVO().setScr(car.getTrailerPic()).setAr(trailerWh[0].doubleValue()/trailerWh[1].doubleValue()));
        infoVO.setLicenseFrontExpired(BaseUtil.timeStamp2Date(car.getLicenseFrontExpired()));
        infoVO.setTransport(car.getTransport());
        infoVO.setTrailer(car.getTrailer());
        infoVO.setEnterpriseName(enterpriseService.getById(car.getEnterpriseId()).getName());
    return new Response(1001,infoVO);
    }
}
