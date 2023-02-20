package com.huzhi.module.module.enterprise.service;

import com.huzhi.module.module.enterprise.entity.Enterprise;
import com.huzhi.module.module.enterprise.mapper.EnterpriseMapper;
import com.huzhi.module.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
     * 插入信息
     * @return
     */
    public BigInteger insert(String name,String legalPersonName,String creditCode,String businessLicensePic,
                          String legalPersonIdFrontPic,String legalPersonIdReversePic){
        Enterprise insertEnterprise=new Enterprise();
        insertEnterprise.setName(name);
        insertEnterprise.setLegalPersonName(legalPersonName);
        insertEnterprise.setCreditCode(creditCode);
        insertEnterprise.setBusinessLicensePic(businessLicensePic);
        insertEnterprise.setLegalPersonIdFrontPic(legalPersonIdFrontPic);
        insertEnterprise.setLegalPersonIdReversePic(legalPersonIdReversePic);
        insertEnterprise.setCreateTime(TimeUtil.getNowTime());
        mapper.insert(insertEnterprise);
        return insertEnterprise.getId();
    }
    /**
     * 修改信息
     * @return
     */
    public BigInteger update(BigInteger id,String name,String legalPersonName,String creditCode,String businessLicensePic,
                          String legalPersonIdFrontPic,String legalPersonIdReversePic,Integer isDeleted){
        Enterprise updateEnterprise=new Enterprise();
        updateEnterprise.setName(name);
        updateEnterprise.setLegalPersonName(legalPersonName);
        updateEnterprise.setCreditCode(creditCode);
        updateEnterprise.setBusinessLicensePic(businessLicensePic);
        updateEnterprise.setLegalPersonIdFrontPic(legalPersonIdFrontPic);
        updateEnterprise.setLegalPersonIdReversePic(legalPersonIdReversePic);
        updateEnterprise.setIsDeleted(isDeleted);
        updateEnterprise.setId(id);
        mapper.update(updateEnterprise);
        return updateEnterprise.getId();
    }
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    public int delete(BigInteger id){
        return mapper.delete(id,TimeUtil.getNowTime());
    }
    /**
     * 通过公司名称获取对应的id列表
     * 用于模糊查询
     * @return              id
     */
    public String getIdByOption(String enterpriseName){
        if (enterpriseName == null || enterpriseName.equals("")){
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
}
