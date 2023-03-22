package com.huzhi.module.module.enterprise.service;

import com.huzhi.module.module.enterprise.entity.Enterprise;
import com.huzhi.module.module.enterprise.mapper.EnterpriseMapper;
import com.huzhi.module.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 公司信息表 服务实现类
 * </p>
 *extends ServiceImpl<EnterpriseMapper, Enterprise> implements EnterpriseService
 * @author huzhi
 * @since 2023-02-03
 */
@Service
public class EnterpriseService  {
    private final EnterpriseMapper mapper;
    @Autowired
    public EnterpriseService(EnterpriseMapper mapper){
        this.mapper=mapper;
    }

    /**
     * 根据id查询（未删除）数据
     * @return
     */
    public Enterprise getById(BigInteger id){
        return mapper.getById(id);
    }
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    public Enterprise extractById(BigInteger id){
        return mapper.extractById(id);
    }
    /**
     * 插入
     */
    public Integer editEnterprise(Enterprise enterprise){
        return mapper.insert(enterprise);
    }
    /**
     * 修改
     */
    public Integer update(Enterprise enterprise){
        return mapper.update(enterprise);
    }
    /**
     * 校验
     */
    private boolean unsafeCheckForEnterprise(String name, String legalPersonName, String phone, String creditCode, String businessLicensePic,
                                          String legalPersonIdFrontPic, String legalPersonIdReversePic){
        if(BaseUtil.isEmpty(name)||BaseUtil.isEmpty(legalPersonName)||BaseUtil.isEmpty(phone)||
                BaseUtil.isEmpty(legalPersonIdFrontPic)||BaseUtil.isEmpty(creditCode)||
                BaseUtil.isEmpty(businessLicensePic)||BaseUtil.isEmpty(legalPersonIdReversePic)){
            throw new RuntimeException("parameter have null");
        }
        return true;
    }
    /**
     * 插入,修改信息
     * @return
     */
    public BigInteger editEnterprise(BigInteger id, String name, String legalPersonName, String phone, String creditCode, String businessLicensePic,
                                     String legalPersonIdFrontPic, String legalPersonIdReversePic, Integer isDeleted){
        if(unsafeCheckForEnterprise(name,legalPersonName,phone,creditCode,businessLicensePic,legalPersonIdFrontPic,legalPersonIdReversePic)){
            Enterprise entity=new Enterprise();
            entity.setName(name);
            entity.setLegalPersonName(legalPersonName);
            entity.setPhone(phone);
            entity.setCreditCode(creditCode);
            entity.setBusinessLicensePic(businessLicensePic);
            entity.setLegalPersonIdFrontPic(legalPersonIdFrontPic);
            entity.setLegalPersonIdReversePic(legalPersonIdReversePic);
            entity.setIsDeleted(isDeleted);
            if(BaseUtil.isEmpty(id)){
                Integer back= editEnterprise(entity);
                entity.setId(BigInteger.valueOf(back));
                return entity.getId();
            }else {
                entity.setId(id);
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
    public Integer delete(BigInteger id){
        return mapper.delete(id, BaseUtil.currentSeconds());
    }
    /**
     * 通过公司名称获取对应的id列表
     * 用于模糊查询
     * @return              id
     */
    public String getIdByOption(String enterpriseName){
        if (BaseUtil.isEmpty(enterpriseName)){
           return null;
        }else {
            List<String> enterpriseIdList=mapper.getIdByOption(enterpriseName);
            StringBuilder enterpriseIdString=new StringBuilder();
            if (enterpriseIdList.size() != 0){
                for (String s: enterpriseIdList) {
                    enterpriseIdString.append(s);
                    enterpriseIdString.append(',');
                }
                enterpriseIdString.deleteCharAt(enterpriseIdString.length()-1);
            }else {
                enterpriseIdString.append("0");
            }
            return enterpriseIdString.toString();
        }
    }
    /**
     * 根据多个id查询（未删除）数据
     * @return
     */
    public List<Enterprise> getByIdList(List<BigInteger> idList){
        String ids=idList.stream().distinct().collect(Collectors.toList()).toString().substring(1);
        StringBuilder enterpriseId=new StringBuilder(ids);
        enterpriseId.deleteCharAt(enterpriseId.length()-1);
        ids=enterpriseId.toString();
        return mapper.getByIdList(ids);
    }
    /**
     * 查询：列表显示所有信息并分页
     */
    public List<Enterprise> getList(String name, String phone, Integer page, Integer pageSize){
        return mapper.getList(name,phone,(page-1)*pageSize,pageSize);
    }
    /**
     * 查询：信息总数附带模糊查询
     */
    public Integer getListTotal(String name, String phone){
        return mapper.getListTotal(name,phone);
    }
}
