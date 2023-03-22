package com.huzhi.console.controller.enterprise;

import com.huzhi.console.annotations.VerifiedUser;
import com.huzhi.console.domain.enterprise.EnterpriseListBaseVO;
import com.huzhi.console.domain.enterprise.EnterpriseListVO;
import com.huzhi.console.domain.enterprise.EnterpriseVO;
import com.huzhi.module.module.enterprise.entity.Enterprise;
import com.huzhi.module.module.enterprise.service.EnterpriseService;
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
public class EnterpriseController {
    private final EnterpriseService enterpriseService;
    private final Integer pageSize=5;
    @Autowired
    public EnterpriseController(EnterpriseService enterpriseService){
        this.enterpriseService=enterpriseService;
    }
    /**
     * 新增合作公司（车辆所属公司）
     * @return
     */
    @RequestMapping("/enterprise/insert")
    public Response EnterpriseInsert(@VerifiedUser User loginUser,
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
            enterpriseService.editEnterprise(null,name,legalPersonName,phone,creditCode,businessLicensePic,
                    legalPersonIdFrontPic,legalPersonIdReversePic,0);
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error EnterpriseInsert:"+e.getMessage());
            return new Response(2002);
        }
    }

    /**
     * 删除合作公司
     * @param loginUser
     * @return
     */
    @RequestMapping("/enterprise/delete")
    public Response EnterpriseDelete(@VerifiedUser User loginUser,
                                 @RequestParam(value = "id") BigInteger id){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        try {
            enterpriseService.delete(id);
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error EnterpriseDelete:"+e.getMessage());
            return new Response(2004);
        }
    }
    /**
     * 修改合作公司信息
     */
    @RequestMapping("/enterprise/update")
    public Response EnterpriseUpdate(@VerifiedUser User loginUser,
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
            enterpriseService.editEnterprise(id,name,legalPersonName,phone,creditCode,businessLicensePic,
                    legalPersonIdFrontPic,legalPersonIdReversePic,isDeleted);
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error EnterpriseUpdate:"+e.getMessage());
            return new Response(2003);
        }
    }
    /**
     * 合作公司列表展示
     */
    @RequestMapping("/enterprise/list")
    public Response EnterpriseList(@VerifiedUser User loginUser,
                               @RequestParam(value = "page") Integer page,
                               @RequestParam(value = "name",required = false) String name,
                               @RequestParam(value = "phone",required = false) String phone){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        name=BaseUtil.isEmpty(name)?null:name.trim();
        phone=BaseUtil.isEmpty(phone)?null:phone.trim();
        List<Enterprise> enterprises=enterpriseService.getList(name,phone,page,pageSize);
        List<EnterpriseListBaseVO> list=new ArrayList<>();
        for (Enterprise e:enterprises) {
            EnterpriseListBaseVO entity=new EnterpriseListBaseVO();
            entity.setId(e.getId());
            entity.setName(e.getName());
            entity.setPhone(e.getPhone());
            entity.setCreditCode(e.getCreditCode());
            list.add(entity);
        }
        EnterpriseListVO enterpriseListVO=new EnterpriseListVO();
        enterpriseListVO.setTotal(enterpriseService.getListTotal(name,phone));
        enterpriseListVO.setPageSize(pageSize);
        enterpriseListVO.setList(list);
        return new Response(1001,enterpriseListVO);
    }
    /**
     * 合作公司详细信息
     */
    @RequestMapping("/enterprise/info")
    public Response EnterpriseInfo(@VerifiedUser User loginUser,
                               @RequestParam(value = "id") BigInteger id){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        Enterprise enterprise=enterpriseService.getById(id);
        EnterpriseVO enterpriseVO=new EnterpriseVO();
        enterpriseVO.setName(enterprise.getName());
        enterpriseVO.setLegalPersonName(enterprise.getLegalPersonName());
        enterpriseVO.setPhone(enterprise.getPhone());
        enterpriseVO.setCreditCode(enterprise.getCreditCode());
        enterpriseVO.setBusinessLicensePic(enterprise.getBusinessLicensePic());
        enterpriseVO.setLegalPersonIdFrontPic(enterprise.getLegalPersonIdFrontPic());
        enterpriseVO.setLegalPersonIdReversePic(enterprise.getLegalPersonIdReversePic());
        enterpriseVO.setCreateTime(BaseUtil.timeStamp2Date(enterprise.getCreateTime()));
        enterpriseVO.setUpdateTime(BaseUtil.timeStamp2Date(enterprise.getUpdateTime()));
        return new Response(1001,enterpriseVO);
    }
}
