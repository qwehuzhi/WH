package com.huzhi.console.controller.company;

import com.huzhi.console.annotations.VerifiedUser;
import com.huzhi.console.domain.company.CompanyListBaseVO;
import com.huzhi.console.domain.company.CompanyListVO;
import com.huzhi.console.domain.company.CompanyVO;
import com.huzhi.module.module.company.entity.Company;
import com.huzhi.module.module.company.service.CompanyService;
import com.huzhi.module.module.user.entity.User;
import com.huzhi.module.response.Response;
import com.huzhi.module.utils.BaseUtil;
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
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    private final Integer pageSize=5;
    /**
     * 新增承运公司
     * @return
     */
    @RequestMapping("/company/insert")
    public Response CompanyInsert(@VerifiedUser User loginUser,
                                 @RequestParam(value = "name") String name,
                                 @RequestParam(value = "legalPersonName") String legalPersonName,
                                 @RequestParam(value = "phone") String phone,
                                 @RequestParam(value = "creditCode") String creditCode,
                                 @RequestParam(value = "businessLicensePic") String businessLicensePic,
                                 @RequestParam(value = "legalPersonIdFrontPic") String legalPersonIdFrontPic,
                                 @RequestParam(value = "legalPersonIdReversePic") String legalPersonIdReversePic
    ){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        name=name.trim();
        legalPersonName=legalPersonName.trim();
        phone=phone.trim();
        creditCode=creditCode.trim();
        try {
            companyService.editForCompany(null,name,legalPersonName,phone,creditCode,businessLicensePic,
                    legalPersonIdFrontPic,legalPersonIdReversePic,0);
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error CompanyInsert:"+e.getMessage());
            return new Response(2002);
        }
    }

    /**
     * 删除承运公司
     * @param loginUser
     * @return
     */
    @RequestMapping("/company/delete")
    public Response CompanyDelete(@VerifiedUser User loginUser,
                                 @RequestParam(value = "id") BigInteger id){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        companyService.delete(id);
        return new Response(1001);
    }
    /**
     * 修改承运公司信息
     */
    @RequestMapping("/company/update")
    public Response CompanyUpdate(@VerifiedUser User loginUser,
                                  @RequestParam(value = "id") BigInteger id,
                                  @RequestParam(value = "name") String name,
                                  @RequestParam(value = "legalPersonName") String legalPersonName,
                                  @RequestParam(value = "phone") String phone,
                                  @RequestParam(value = "creditCode") String creditCode,
                                  @RequestParam(value = "businessLicensePic") String businessLicensePic,
                                  @RequestParam(value = "legalPersonIdFrontPic") String legalPersonIdFrontPic,
                                  @RequestParam(value = "legalPersonIdReversePic") String legalPersonIdReversePic,
                                  @RequestParam(value = "isDeleted")Integer isDeleted
    ){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        name=name.trim();
        legalPersonName=legalPersonName.trim();
        phone=phone.trim();
        creditCode=creditCode.trim();
        try {
            companyService.editForCompany(id,name,legalPersonName,phone,creditCode,businessLicensePic,
                    legalPersonIdFrontPic,legalPersonIdReversePic,isDeleted);
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error CompanyUpdate:"+e.getMessage());
            return new Response(2003);
        }
    }
    /**
     * 承运公司列表展示
     */
    @RequestMapping("/company/list")
    public Response CompanyList(@VerifiedUser User loginUser,
                               @RequestParam(value = "page") Integer page,
                               @RequestParam(value = "name",required = false) String name,
                               @RequestParam(value = "phone",required = false) String phone){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        name=BaseUtil.isEmpty(name)?null:name.trim();
        phone=BaseUtil.isEmpty(phone)?null:phone.trim();
        List<Company> Companys=companyService.getListForCompany(name,phone,page,pageSize);
        List<CompanyListBaseVO> list=new ArrayList<>();
        for (Company c:Companys) {
            CompanyListBaseVO entity=new CompanyListBaseVO();
            entity.setId(c.getId());
            entity.setName(c.getName());
            entity.setPhone(c.getPhone());
            entity.setCreditCode(c.getCreditCode());
            list.add(entity);
        }
        CompanyListVO companyListVO=new CompanyListVO();
        companyListVO.setTotal(companyService.getCompanyListTotal(name,phone));
        companyListVO.setPageSize(pageSize);
        companyListVO.setList(list);
        return new Response(1001,companyListVO);
    }
    /**
     * 承运公司详细信息
     */
    @RequestMapping("/company/info")
    public Response CompanyInfo(@VerifiedUser User loginUser,
                               @RequestParam(value = "id") BigInteger id){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        Company company=companyService.getById(id);
        CompanyVO companyVO=new CompanyVO();
        companyVO.setName(company.getName());
        companyVO.setLegalPersonName(company.getLegalPersonName());
        companyVO.setPhone(company.getPhone());
        companyVO.setCreditCode(company.getCreditCode());
        companyVO.setBusinessLicensePic(company.getBusinessLicensePic());
        companyVO.setLegalPersonIdFrontPic(company.getLegalPersonIdFrontPic());
        companyVO.setLegalPersonIdReversePic(company.getLegalPersonIdReversePic());
        companyVO.setCreateTime(BaseUtil.timeStamp2Date(company.getCreateTime()));
        companyVO.setUpdateTime(BaseUtil.timeStamp2Date(company.getUpdateTime()));
        return new Response(1001,companyVO);
    }
}
