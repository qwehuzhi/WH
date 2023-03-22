package com.huzhi.app.controller.driver;

import com.alibaba.fastjson.JSONObject;
import com.huzhi.app.domain.ImageVO;
import com.huzhi.app.domain.car.CarListBaseVO;
import com.huzhi.app.domain.car.CarListVO;
import com.huzhi.app.domain.car.WrapperPageVO;
import com.huzhi.app.domain.driver.DriverListBaseVO;
import com.huzhi.app.domain.driver.DriverListVO;
import com.huzhi.app.domain.driver.DriverVO;
import com.huzhi.app.domain.driver.DriverWpVO;
import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.driver.entity.Driver;
import com.huzhi.module.module.driver.service.DriverService;
import com.huzhi.module.module.enterprise.entity.Enterprise;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class DriverController {
    private static final int pageSize =5;
    private final DriverService driverService;
    @Autowired
    public DriverController(DriverService driverService){
        this.driverService=driverService;
    }
    @RequestMapping("/driver/list")
    public Response driverList(@RequestParam(value = "wp",required = false)String wp,
                               @RequestParam(value = "name",required = false) String name,
                               @RequestParam(value = "phone",required = false) String phone){
        DriverWpVO wpMassage=new DriverWpVO();
        if (BaseUtil.isEmpty(wp)){
            name=BaseUtil.isEmpty(name)?null:name.trim();
            phone=BaseUtil.isEmpty(phone)?null:phone.trim();
            wpMassage.setPage(1);
            wpMassage.setName(name);
            wpMassage.setPhone(phone);
        }else {
            wpMassage= JSONObject.parseObject(Base64.getDecoder().decode(wp.getBytes(StandardCharsets.UTF_8)), WrapperPageVO.class);
            wpMassage.setPage(wpMassage.getPage()+1);
        }
        String wpBase= Base64.getUrlEncoder().encodeToString(JSONObject.toJSONString(wpMassage).getBytes(StandardCharsets.UTF_8));
        List<Driver> drivers=driverService.getDriverList(wpMassage.getName(),wpMassage.getPhone(),wpMassage.getPage(), pageSize);
        List<DriverListBaseVO> list=new ArrayList<>();
        for (Driver e : drivers) {
                DriverListBaseVO entity = new DriverListBaseVO();
                entity.setId(e.getId());
                entity.setName(e.getName());
                entity.setPhone(e.getPhone());
                entity.setExamineStatus(e.getExamineStatus());
                entity.setExamineRemarks(e.getExamineRemarks());
                list.add(entity);
        }
        DriverListVO driverListVO=new DriverListVO();
        driverListVO.setList(list);
        driverListVO.setWp(wpBase);
        driverListVO.setIsEnd(drivers.size() < pageSize);
        return new Response(1001,driverListVO);
    }
    @RequestMapping("/driver/info")
    public Response driverInfo(@RequestParam(value = "id")BigInteger id){
        Driver driver=driverService.getById(id);
        DriverVO info=new DriverVO();
        Integer[] idNumberFrontWh= ImageUtil.getImageWidthAndHeight(driver.getIdNumberFrontPic());
        Integer[] idNumberBackWh= ImageUtil.getImageWidthAndHeight(driver.getIdNumberBackPic());
        Integer[] drivingLicenseFrontWh= ImageUtil.getImageWidthAndHeight(driver.getDrivingLicenseFrontPic());
        Integer[] drivingLicenseBackWh= ImageUtil.getImageWidthAndHeight(driver.getDrivingLicenseBackPic());
        Integer[] qualificationCertificateWh= ImageUtil.getImageWidthAndHeight(driver.getQualificationCertificatePic());
        info.setName(driver.getName());
        info.setPhone(driver.getPhone());
        info.setRemarks(driver.getRemarks());
        info.setIdNumberExpired(BaseUtil.timeStamp2Date(driver.getIdNumberExpired()));
        info.setDrivingLicenseAuthority(driver.getDrivingLicenseAuthority());
        info.setDrivingLicenseName(driver.getDrivingLicenseName());
        info.setDrivingLicenseExpired(BaseUtil.timeStamp2Date(driver.getDrivingLicenseExpired()));
        info.setModel(driver.getModel());
        info.setIdNumberFrontPic(new ImageVO().setScr(driver.getIdNumberFrontPic()).setAr(idNumberFrontWh[0].doubleValue()/idNumberFrontWh[1].doubleValue()));
        info.setIdNumberBackPic(new ImageVO().setScr(driver.getIdNumberBackPic()).setAr(idNumberBackWh[0].doubleValue()/idNumberBackWh[1].doubleValue()));
        info.setIdNumberFrontPic(new ImageVO().setScr(driver.getIdNumberFrontPic()).setAr(drivingLicenseFrontWh[0].doubleValue()/drivingLicenseFrontWh[1].doubleValue()));
        info.setDrivingLicenseBackPic(new ImageVO().setScr(driver.getDrivingLicenseBackPic()).setAr(drivingLicenseBackWh[0].doubleValue()/drivingLicenseBackWh[1].doubleValue()));
        info.setQualificationCertificatePic(new ImageVO().setScr(driver.getQualificationCertificatePic()).setAr(qualificationCertificateWh[0].doubleValue()/qualificationCertificateWh[1].doubleValue()));
        info.setExamineStatus(driver.getExamineStatus());
        info.setExamineRemarks(driver.getExamineRemarks());
        return new Response(1001,info);
    }
}
