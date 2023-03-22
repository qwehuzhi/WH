package com.huzhi.module.module.waybill.service;

import com.huzhi.module.code.WaybillExamineCode;
import com.huzhi.module.module.waybill.entity.Waybill;
import com.huzhi.module.module.waybill.mapper.WaybillMapper;
import com.huzhi.module.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 运单信息表 服务类
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Service
public class WaybillService{
    private final WaybillMapper mapper;
    private final String needExamine=WaybillExamineCode.ExamineNew.getId()+","+WaybillExamineCode.ExamineFail.getId();
    @Autowired
    public WaybillService(WaybillMapper mapper){
        this.mapper=mapper;
    }
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    public Waybill getById(BigInteger id){
        return mapper.getById(id);
    }
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    public Waybill extractById(BigInteger id){
        return mapper.extractById(id);
    }
    /**
     * 插入信息
     * @return
     */
    public Integer insert(Waybill waybill){
        return mapper.insert(waybill);
    }
    /**
     * 修改信息
     * @return
     */
    public Integer update(Waybill waybill){
        return mapper.update(waybill);
    }
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    public int delete(BigInteger id){
        return mapper.delete(id, BaseUtil.currentSeconds());
    }
    /**
     * 时间校验
     */
    public boolean timeExamine(Integer deliveryTime,Integer arrivalTime,BigInteger carId,BigInteger driverId){
        List<BigInteger> bigIntegers = mapper.timeExamine(deliveryTime, arrivalTime, carId, driverId);
        if(bigIntegers.size() == 0){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 列表显示所有运单信息并分页，附带模糊查询
     */
    public List<Waybill> getList(Integer page,Integer pageSize,String companyIds,String clientIds,String driverIds,
                                 String carIds){
        if(BaseUtil.isEmpty(page)){
            throw new RuntimeException("page is null");
        }
        if(BaseUtil.isEmpty(pageSize)){
            throw new RuntimeException("pageSize is null");
        }
        return mapper.getList(companyIds,clientIds,driverIds,carIds,(page-1)*pageSize,
                                pageSize,needExamine,0);
    }
    /**
     * 查询运单总数附带模糊查询
     */
    public Integer getListTotal(String companyIds,String clientIds,String driverIds,
                                String carIds){
        return mapper.getTotal(companyIds,clientIds,driverIds,carIds,needExamine,0);
    }
    /**
     * 审核
     */
    public Integer examine(BigInteger id,Integer examineStatus,String examineRemarks){
        if(BaseUtil.isEmpty(id) || BaseUtil.isEmpty(examineStatus)){
            throw new RuntimeException("Parameter does not exist");
        }
        return mapper.examine(id,examineStatus,examineRemarks,BaseUtil.currentSeconds());
    }
}
