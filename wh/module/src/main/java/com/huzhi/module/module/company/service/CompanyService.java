package com.huzhi.module.module.company.service;

import com.huzhi.module.module.company.entity.Company;
import com.huzhi.module.module.company.mapper.CompanyMapper;
import com.huzhi.module.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 承运公司表 服务类
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Service
public class CompanyService{
    private final CompanyMapper mapper;
    @Autowired
    public CompanyService(CompanyMapper mapper){
        this.mapper=mapper;
    }
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    public Company getById(BigInteger id){
        return mapper.getById(id);
    }
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    public Company extractById(BigInteger id){
        return mapper.extractById(id);
    }
    /**
     * 插入
     */
    public Integer insert(Company company){
        return mapper.insert(company);
    }
    /**
     * 修改
     */
    public Integer update(Company company){
        return mapper.update(company);
    }
    /**
     * 校验
     */
    private boolean unsafeCheckForCompany(String name, String legalPersonName, String phone, String creditCode, String businessLicensePic,
                                          String legalPersonIdFrontPic, String legalPersonIdReversePic){
        if(BaseUtil.isEmpty(name) || BaseUtil.isEmpty(legalPersonName) || BaseUtil.isEmpty(phone) ||
                BaseUtil.isEmpty(legalPersonIdFrontPic) || BaseUtil.isEmpty(creditCode) ||
                BaseUtil.isEmpty(businessLicensePic) || BaseUtil.isEmpty(legalPersonIdReversePic)){
            throw new RuntimeException("parameter have null");
        }
        return true;
    }
    /**
     * 插入信息
     * @return
     */
    public BigInteger editForCompany(BigInteger id, String name, String legalPersonName, String phone, String creditCode, String businessLicensePic,
                                     String legalPersonIdFrontPic, String legalPersonIdReversePic, Integer isDeleted){
        if(unsafeCheckForCompany(name,legalPersonName,phone,creditCode,businessLicensePic,legalPersonIdFrontPic,legalPersonIdReversePic)){
            Company entity=new Company();
            entity.setName(name);
            entity.setLegalPersonName(legalPersonName);
            entity.setPhone(phone);
            entity.setCreditCode(creditCode);
            entity.setBusinessLicensePic(businessLicensePic);
            entity.setLegalPersonIdFrontPic(legalPersonIdFrontPic);
            entity.setLegalPersonIdReversePic(legalPersonIdReversePic);
            entity.setIsDeleted(isDeleted);
            if(BaseUtil.isEmpty(id)){
                return BigInteger.valueOf(insert(entity));
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
     * 查询：列表显示所有信息并分页
     */
    public List<Company> getListForCompany(String name, String phone, Integer page, Integer pageSize){
        return mapper.getList(name,phone,(page-1)*pageSize,pageSize);
    }
    /**
     * 查询：信息总数附带模糊查询
     */
    public Integer getCompanyListTotal(String name, String phone){
        return mapper.getListTotal(name,phone);
    }
    /**
     * 模糊查询返回id
     */
    public String getCompanyIdByName(String companyName){
            List<String> IdList=mapper.getIdByOption(companyName);
            String IdsToString="";
            if (IdList.size() != 0){
                for (String s: IdList) {
                    IdsToString=BaseUtil.implodeSearchParam(IdsToString,s);
                }
            }
            return IdsToString;
    }
    /**
     * 通过多个id获取
     */
    public List<Company> getListByIdList(List<BigInteger> idList){
        String ids=idList.stream().distinct().collect(Collectors.toList()).toString().substring(1);
        StringBuilder enterpriseId=new StringBuilder(ids);
        enterpriseId.deleteCharAt(enterpriseId.length()-1);
        ids=enterpriseId.toString();
        return mapper.getNameByIds(ids);
    }
    /**
     *模糊搜索审核通过的车辆
     */
    public List<Company> getBySelect(String name){
        return mapper.getBySelect(name);
    }
}
