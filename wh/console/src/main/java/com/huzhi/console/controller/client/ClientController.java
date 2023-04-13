package com.huzhi.console.controller.client;

import com.huzhi.console.annotations.VerifiedUser;
import com.huzhi.console.domain.client.ClientListBaseVO;
import com.huzhi.console.domain.client.ClientListVO;
import com.huzhi.console.domain.client.ClientVO;
import com.huzhi.module.module.client.entity.Client;
import com.huzhi.module.module.client.service.ClientService;
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
public class ClientController {
    @Autowired
    private ClientService clientService;
    private final int pageSize=5;

    /**
     * 新增客户
     * @return
     */
    @RequestMapping("/client/insert")
    public Response ClientInsert(@VerifiedUser User loginUser,
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
            clientService.editClient(null,name,legalPersonName,phone,creditCode,businessLicensePic,
                    legalPersonIdFrontPic,legalPersonIdReversePic,0);
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error ClientInsert:"+e.getMessage());
            return new Response(2002);
        }
    }

    /**
     * 删除客户
     * @param loginUser
     * @return
     */
    @RequestMapping("/client/delete")
    public Response ClientDelete(@VerifiedUser User loginUser,
                                 @RequestParam(value = "id") BigInteger id){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        clientService.delete(id);
        return new Response(1001);
    }
    /**
     * 修改客户信息
     */
    @RequestMapping("/client/update")
    public Response ClientUpdate(@VerifiedUser User loginUser,
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
            clientService.editClient(id,name,legalPersonName,phone,creditCode,businessLicensePic,
                    legalPersonIdFrontPic,legalPersonIdReversePic,isDeleted);
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error ClientUpdate:"+e.getMessage());
            return new Response(2003);
        }

    }
    /**
     * 客户列表展示
     */
    @RequestMapping("/client/list")
    public Response ClientList(@VerifiedUser User loginUser,
                                 @RequestParam(value = "page") Integer page,
                                 @RequestParam(value = "name",required = false) String name,
                                 @RequestParam(value = "phone",required = false) String phone){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        name=BaseUtil.isEmpty(name)?null:name.trim();
        phone=BaseUtil.isEmpty(phone)?null:phone.trim();
        try {
            List<Client> clients=clientService.getListForClient(name,phone,page,pageSize);
            List<ClientListBaseVO> list=new ArrayList<>();
            for (Client c:clients) {
                ClientListBaseVO entity=new ClientListBaseVO();
                entity.setId(c.getId());
                entity.setName(c.getName());
                entity.setPhone(c.getPhone());
                entity.setCreditCode(c.getCreditCode());
                list.add(entity);
            }
            ClientListVO clientListVO=new ClientListVO();
            clientListVO.setTotal(clientService.getClientListTotal(name,phone));
            clientListVO.setPageSize(pageSize);
            clientListVO.setList(list);
            return new Response(1001,clientListVO);
        }catch (RuntimeException e){
            log.info("Error ClientList:"+e.getMessage());
            return new Response(2005);
        }
    }
    /**
     * 客户详细信息
     */
    @RequestMapping("/client/info")
    public Response ClientInfo(@VerifiedUser User loginUser,
                               @RequestParam(value = "id") BigInteger id){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        Client client=clientService.getById(id);
        ClientVO clientVO=new ClientVO();
        clientVO.setName(client.getName());
        clientVO.setLegalPersonName(client.getLegalPersonName());
        clientVO.setPhone(client.getPhone());
        clientVO.setCreditCode(client.getCreditCode());
        clientVO.setBusinessLicensePic(client.getBusinessLicensePic());
        clientVO.setLegalPersonIdFrontPic(client.getLegalPersonIdFrontPic());
        clientVO.setLegalPersonIdReversePic(client.getLegalPersonIdReversePic());
        clientVO.setCreateTime(BaseUtil.timeStamp2Date(client.getCreateTime()));
        clientVO.setUpdateTime(BaseUtil.timeStamp2Date(client.getUpdateTime()));
        return new Response(1001,clientVO);
    }
}
