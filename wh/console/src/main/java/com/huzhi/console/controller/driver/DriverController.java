package com.huzhi.console.controller.driver;

import com.huzhi.console.annotations.VerifiedUser;
import com.huzhi.console.domain.utils.ImageVO;
import com.huzhi.console.domain.driver.DriverListBaseVO;
import com.huzhi.console.domain.driver.DriverListVO;
import com.huzhi.console.domain.driver.DriverVO;
import com.huzhi.module.module.car.service.BaseExamineService;
import com.huzhi.module.module.driver.entity.Driver;
import com.huzhi.module.module.driver.service.DriverService;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class DriverController {
    @Autowired
    private DriverService driverService;
    @Autowired
    private BaseExamineService examineService;
    private final int pageSize=5;

    /**
     *  新增司机信息
     * @return
     */
    @RequestMapping("/driver/insert")
    public Response driverInsert(@VerifiedUser User loginUser,
                                 @RequestParam(value = "name") String name,
                                 @RequestParam(value = "phone") String phone,
                                 @RequestParam(value = "idNumber") String idNumber,
                                 @RequestParam(value = "remarks",required = false) String remarks,
                                 @RequestParam(value = "idNumberExpired") Integer idNumberExpired,
                                 @RequestParam(value = "drivingLicenseName") String drivingLicenseName,
                                 @RequestParam(value = "drivingLicenseAuthority") String drivingLicenseAuthority,
                                 @RequestParam(value = "drivingLicenseExpired") Integer drivingLicenseExpired,
                                 @RequestParam(value = "model") String model,
                                 @RequestParam(value = "idNumberFrontPic") String idNumberFrontPic,
                                 @RequestParam(value = "idNumberBackPic") String idNumberBackPic,
                                 @RequestParam(value = "drivingLicenseFrontPic") String drivingLicenseFrontPic,
                                 @RequestParam(value = "drivingLicenseBackPic") String drivingLicenseBackPic,
                                 @RequestParam(value = "qualificationCertificatePic") String qualificationCertificatePic
    ){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        name=name.trim();
        phone=phone.trim();
        idNumber=idNumber.trim();
        remarks=BaseUtil.isEmpty(remarks)?null:remarks.trim();
        drivingLicenseName=drivingLicenseName.trim();
        drivingLicenseAuthority=drivingLicenseAuthority.trim();
        try {
            driverService.editForDriver(null,name,idNumber,phone,remarks,drivingLicenseName,drivingLicenseAuthority,model,
                    idNumberFrontPic,idNumberBackPic,drivingLicenseFrontPic,drivingLicenseBackPic,qualificationCertificatePic,
                    idNumberExpired,drivingLicenseExpired,0,null,null);
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error editForDriver:"+e.getMessage());
            return new Response(2002);
        }
    }

    /**
     * 修改司机信息
     */
    @RequestMapping("/driver/update")
    public Response driverUpdate(@VerifiedUser User loginUser,
                                 @RequestParam(value = "id") BigInteger id,
                                 @RequestParam(value = "name") String name,
                                 @RequestParam(value = "phone") String phone,
                                 @RequestParam(value = "idNumber") String idNumber,
                                 @RequestParam(value = "remarks",required = false) String remarks,
                                 @RequestParam(value = "idNumberExpired") Integer idNumberExpired,
                                 @RequestParam(value = "drivingLicenseName") String drivingLicenseName,
                                 @RequestParam(value = "drivingLicenseAuthority") String drivingLicenseAuthority,
                                 @RequestParam(value = "drivingLicenseExpired") Integer drivingLicenseExpired,
                                 @RequestParam(value = "model") String model,
                                 @RequestParam(value = "idNumberFrontPic") String idNumberFrontPic,
                                 @RequestParam(value = "idNumberBackPic") String idNumberBackPic,
                                 @RequestParam(value = "drivingLicenseFrontPic") String drivingLicenseFrontPic,
                                 @RequestParam(value = "drivingLicenseBackPic") String drivingLicenseBackPic,
                                 @RequestParam(value = "qualificationCertificatePic") String qualificationCertificatePic,
                                 @RequestParam(value = "examineStatus") Integer examineStatus,
                                 @RequestParam(value = "examineRemarks",required = false) String examineRemarks,
                                 @RequestParam(value = "isDeleted") Integer isDeleted
                                 ){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        name=name.trim();
        phone=phone.trim();
        idNumber=idNumber.trim();
        remarks=BaseUtil.isEmpty(remarks)?null:remarks.trim();
        drivingLicenseName=drivingLicenseName.trim();
        drivingLicenseAuthority=drivingLicenseAuthority.trim();
        examineRemarks=BaseUtil.isEmpty(examineRemarks)?null:examineRemarks.trim();
        try {
            driverService.editForDriver(id,name,idNumber,phone,remarks,drivingLicenseName,drivingLicenseAuthority,model,idNumberFrontPic,
                    idNumberBackPic,drivingLicenseFrontPic,drivingLicenseBackPic,qualificationCertificatePic,
                    idNumberExpired,drivingLicenseExpired,isDeleted,examineRemarks,examineStatus);
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error editForDriver:"+e.getMessage());
            return new Response(2003);
        }
    }
    /**
     * 司机列表展示
     */
    @RequestMapping("/driver/list")
    public Response driverList(@VerifiedUser User loginUser,
                                 @RequestParam(value = "page")Integer page,
                                 @RequestParam(value = "name",required = false)String name,
                                 @RequestParam(value = "phone",required = false) String phone){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        name=BaseUtil.isEmpty(name)?null:name.trim();
        phone=BaseUtil.isEmpty(phone)?null:phone.trim();
        List<Driver> drivers;
        drivers=driverService.getDriverList(name,phone,page,pageSize);
        DriverListVO driverListVO=new DriverListVO();
        List<DriverListBaseVO> list=new ArrayList<>();
        for(Driver d:drivers){
            DriverListBaseVO entity=new DriverListBaseVO();
            entity.setId(d.getId());
            entity.setName(d.getName());
            entity.setPhone(d.getPhone());
            entity.setExamineStatus(d.getExamineStatus());
            list.add(entity);
        }
        driverListVO.setList(list);
        driverListVO.setTotal(driverService.getTotal(name, phone));
        driverListVO.setPageSize(pageSize);
        return new Response(1001,driverListVO);
    }
    /**
     * 删除司机信息
     */
    @RequestMapping("/driver/delete")
    public Response driverDelete(@VerifiedUser User loginUser,
                                 @RequestParam(value = "id") BigInteger id) {
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        driverService.delete(id);
        return new Response(1001);
    }
    /**
     * 司机详细信息
     */
    @RequestMapping("/driver/info")
    public Response driverInfo(@VerifiedUser User loginUser,
                                 @RequestParam(value = "id") BigInteger id){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
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
        info.setCreateTime(BaseUtil.timeStamp2Date(driver.getCreateTime()));
        info.setUpdateTime(BaseUtil.timeStamp2Date(driver.getUpdateTime()));
        return new Response(1001,info);
    }
    /**
     * 司机审核
     */
    @RequestMapping("/driver/examine")
    public Response driverExamine(@VerifiedUser User loginUser,
                                  @RequestParam(value = "id") BigInteger id,
                                  @RequestParam(value = "examineStatus") Integer examineStatus,
                                  @RequestParam(value = "examineRemarks",required = false) String examineRemarks) {
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        examineRemarks=BaseUtil.isEmpty(examineRemarks)?null:examineRemarks.trim();
        try {
            examineService.DriverExamine(id,examineStatus,examineRemarks,loginUser.getId(),BaseUtil.currentSeconds());
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error driverExamine:"+e.getMessage());
            return new Response(2011);
        }
    }
}
