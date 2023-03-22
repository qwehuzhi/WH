package com.huzhi.module.module.client.service;

import com.huzhi.module.module.client.entity.Client;
import com.huzhi.module.module.client.mapper.ClientMapper;
import com.huzhi.module.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户公司表 服务类
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Service
public class ClientService{
    private final ClientMapper mapper;
    @Autowired
    public ClientService(ClientMapper mapper){
        this.mapper=mapper;
    }
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    public Client getById(BigInteger id){
        return mapper.getById(id);
    }
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    public Client extractById(BigInteger id){
        return mapper.extractById(id);
    }
    /**
     * 校验
     */
    private boolean unsafeCheckForClient(String name, String legalPersonName, String phone, String creditCode, String businessLicensePic,
                                      String legalPersonIdFrontPic, String legalPersonIdReversePic){
        if(BaseUtil.isEmpty(name) || BaseUtil.isEmpty(legalPersonName) || BaseUtil.isEmpty(phone) ||
                BaseUtil.isEmpty(legalPersonIdFrontPic) || BaseUtil.isEmpty(creditCode) ||
                BaseUtil.isEmpty(businessLicensePic) || BaseUtil.isEmpty(legalPersonIdReversePic)){
            throw new RuntimeException("parameter have null");
        }
        return true;
    }
    /**
     * 插入
     */
    public Integer insert(Client client){
        return mapper.insert(client);
    }
    /**
     * 修改
     */
    public Integer update(Client client){
        return mapper.update(client);
    }
    /**
     * 插入和修改信息
     *
     * @return
     */
    public BigInteger editClient(BigInteger id, String name, String legalPersonName, String phone, String creditCode, String businessLicensePic,
                                 String legalPersonIdFrontPic, String legalPersonIdReversePic, Integer isDeleted){
        if(unsafeCheckForClient(name,legalPersonName,phone,creditCode,businessLicensePic,
                legalPersonIdFrontPic,legalPersonIdReversePic)){
            Client entity=new Client();
            entity.setName(name);
            entity.setLegalPersonName(legalPersonName);
            entity.setPhone(phone);
            entity.setCreditCode(creditCode);
            entity.setBusinessLicensePic(businessLicensePic);
            entity.setLegalPersonIdFrontPic(legalPersonIdFrontPic);
            entity.setLegalPersonIdReversePic(legalPersonIdReversePic);
            entity.setIsDeleted(isDeleted);
            if(!BaseUtil.isEmpty(id)){
                return BigInteger.valueOf(insert(entity));
            }else {
                entity.setId(id);
                update(entity);
                return id;
            }
        }else {
         return null;
        }
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
    public List<Client> getListForClient(String name, String phone, Integer page, Integer pageSize){
        if(BaseUtil.isEmpty(page) || BaseUtil.isEmpty(pageSize)){
            throw new RuntimeException("page or pageSize is null");
        }
        return mapper.getList(name,phone,(page-1)*pageSize,pageSize);
    }
    /**
     * 查询：信息总数附带模糊查询
     */
    public Integer getClientListTotal(String name, String phone){
        return mapper.getListTotal(name,phone);
    }
    /**
     * 模糊查询返回id
     */
    public String getClientIdByName(String clientName){
            List<String> IdList=mapper.getIdByOption(clientName);
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
    public List<Client> getListByIdList(List<BigInteger> idList){
        String ids=idList.stream().distinct().collect(Collectors.toList()).toString().substring(1);
        StringBuilder enterpriseId=new StringBuilder(ids);
        enterpriseId.deleteCharAt(enterpriseId.length()-1);
        ids=enterpriseId.toString();
        return mapper.getNameByIds(ids);
    }
    /**
     *模糊搜索审核通过的车辆
     */
    public List<Client> getBySelect(String name){
        return mapper.getBySelect(name);
    }
    /**
     * 计算前一天的单日增量
     */
    public Integer getYesterdayIncrementForClient(){
        Integer begin=BaseUtil.getCalendar0Time();
        return mapper.getYesterdayIncrementForClient(begin,begin+86400);
    }
}
