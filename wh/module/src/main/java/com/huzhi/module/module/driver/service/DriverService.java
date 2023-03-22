package com.huzhi.module.module.driver.service;

import com.huzhi.module.module.driver.entity.Driver;
import com.huzhi.module.module.driver.mapper.DriverMapper;
import com.huzhi.module.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 司机信息表 服务类
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Service
public class DriverService{
    private final DriverMapper mapper;
    @Autowired
    public DriverService(DriverMapper mapper){
        this.mapper=mapper;
    }
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    public Driver getById(BigInteger id){
        Driver driver=mapper.getById(id);
        return driver;
    }
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    public Driver extractById(BigInteger id){
        return mapper.extractById(id);
    }
    /**
     * 插入
     */
    public Integer editForDriver(Driver driver){
        return mapper.insert(driver);
    }
    /**
     * 修改
     */
    public Integer update(Driver driver){
        return mapper.update(driver);
    }
    /**
     * 校验
     */
    private boolean unsafeCheckForDriver(String name, String idNumber, String phone, String drivingLicenseName,
                                      String drivingLicenseAuthority, String model, String idNumberFrontPic, String idNumberBackPic,
                                      String drivingLicenseFrontPic, String drivingLicenseBackPic, String qualificationCertificatePic,
                                      Integer idNumberExpired, Integer drivingLicenseExpired,Integer isDeleted){
        if(BaseUtil.isEmpty(name) || BaseUtil.isEmpty(idNumber) || BaseUtil.isEmpty(phone) ||
                BaseUtil.isEmpty(drivingLicenseName) || BaseUtil.isEmpty(drivingLicenseAuthority) ||
                BaseUtil.isEmpty(model) || BaseUtil.isEmpty(idNumberFrontPic) ||
                BaseUtil.isEmpty(idNumberBackPic) || BaseUtil.isEmpty(drivingLicenseFrontPic) ||
                BaseUtil.isEmpty(drivingLicenseBackPic) || BaseUtil.isEmpty(qualificationCertificatePic) ||
                BaseUtil.isEmpty(idNumberExpired) || BaseUtil.isEmpty(drivingLicenseExpired) ||
                BaseUtil.isEmpty(isDeleted)){
            throw new RuntimeException("parameter have null");
        }
        if(!drivingLicenseName.equals(name)){
            throw new RuntimeException("drivingLicenseName is fail");
        }
        return true;
    }

    /**
     * 插入 修改
     * @return
     */
    public BigInteger editForDriver(BigInteger id, String name, String idNumber, String phone, String remarks, String drivingLicenseName,
                                    String drivingLicenseAuthority, String model, String idNumberFrontPic, String idNumberBackPic,
                                    String drivingLicenseFrontPic, String drivingLicenseBackPic, String qualificationCertificatePic,
                                    Integer idNumberExpired, Integer drivingLicenseExpired, Integer isDeleted, String examineRemarks, Integer examineStatus) {
        if (unsafeCheckForDriver(name, idNumber, phone, drivingLicenseName, drivingLicenseAuthority, model, idNumberFrontPic, idNumberBackPic,
                drivingLicenseFrontPic, drivingLicenseBackPic, qualificationCertificatePic, idNumberExpired, drivingLicenseExpired, isDeleted)) {
            Driver entity = new Driver();
            entity.setName(name);
            entity.setIdNumber(idNumber);
            entity.setPhone(phone);
            entity.setRemarks(remarks);
            entity.setDrivingLicenseName(drivingLicenseName);
            entity.setDrivingLicenseAuthority(drivingLicenseAuthority);
            entity.setModel(model);
            entity.setIdNumberFrontPic(idNumberFrontPic);
            entity.setIdNumberBackPic(idNumberBackPic);
            entity.setDrivingLicenseFrontPic(drivingLicenseFrontPic);
            entity.setDrivingLicenseBackPic(drivingLicenseBackPic);
            entity.setQualificationCertificatePic(qualificationCertificatePic);
            entity.setIdNumberExpired(idNumberExpired);
            entity.setDrivingLicenseExpired(drivingLicenseExpired);
            entity.setIsDeleted(isDeleted);
            if (BaseUtil.isEmpty(id)) {
                entity.setCreateTime(BaseUtil.currentSeconds());
                return BigInteger.valueOf(mapper.insert(entity));
            } else {
                entity.setId(id);
                entity.setExamineRemarks(examineRemarks);
                entity.setExamineStatus(examineStatus);
                update(entity);
                return id;
            }
        }
        return null;
    }
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    public int delete(BigInteger id){
        return mapper.delete(id,BaseUtil.currentSeconds());
    }
    /**
     * 司机列表模糊查询
     */
    public List<Driver> getDriverList(String name, String phone, Integer page, Integer pageSize){
        return mapper.getList(name,phone,(page-1)*pageSize,pageSize);
    }
    /**
     * 司机模糊查询的列表总数
     */
    public Integer getTotal(String name, String phone){
        return mapper.getTotal(name, phone);
    }
    /**
     * 审核
     */
    public Integer examine(BigInteger id,Integer examineStatus,String examineRemarks){
        if(BaseUtil.isEmpty(id) || BaseUtil.isEmpty(examineStatus)){
            throw new RuntimeException("id or examineStatus is null");
        }
        return mapper.examine(id, examineStatus, examineRemarks, BaseUtil.currentSeconds());
    }
    /**
     * 模糊查询返回id
     */
    public String getIdByOption(String driverName){
            List<String> IdList=mapper.getIdByOption(driverName);
            StringBuilder IdsToString=new StringBuilder();
            if (IdList.size() != 0){
                for (String s: IdList) {
                    IdsToString.append(s);
                    IdsToString.append(',');
                }
                IdsToString.deleteCharAt(IdsToString.length()-1);
            }else {
                IdsToString.append("0");
            }
            return IdsToString.toString();

    }
    /**
     * 通过多个id获取
     */
    public List<Driver> getListByIdList(List<BigInteger> idList){
        String ids=idList.stream().distinct().collect(Collectors.toList()).toString().substring(1);
        StringBuilder enterpriseId=new StringBuilder(ids);
        enterpriseId.deleteCharAt(enterpriseId.length()-1);
        ids=enterpriseId.toString();
        return mapper.getNameByIds(ids);
    }
    /**
     * 模糊搜索审核通过的车辆
     */
    public List<Driver> getBySelect(String name,String phone){
        return mapper.getBySelect(name,phone);
    }
    /**
     * 计算前一天的单日增量
     */
    public Integer getYesterdayIncrementForDriver(){
        Integer begin=BaseUtil.getCalendar0Time();
        return mapper.getYesterdayIncrementForDriver(begin,begin+86400);
    }
}
