package com.huzhi.module.module.waybillSnapshot.service;

import com.huzhi.module.module.waybillSnapshot.entity.WaybillSnapshot;
import com.huzhi.module.module.waybillSnapshot.mapper.WaybillSnapshotMapper;
import com.huzhi.module.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 运单快照表 服务类
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Service
public class WaybillSnapshotService{
    private final WaybillSnapshotMapper mapper;
    @Autowired
    public WaybillSnapshotService(WaybillSnapshotMapper mapper){
        this.mapper=mapper;
    }
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    public WaybillSnapshot getById(BigInteger id){
        return mapper.getById(id);
    }
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    public WaybillSnapshot extractById(BigInteger id){
        return mapper.extractById(id);
    }
    /**
     * 插入信息
     * @return
     */
    public Integer insert(WaybillSnapshot waybillSnapshot){
        return mapper.insert(waybillSnapshot);
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
    public List<WaybillSnapshot> getList(String clientName, String clientPhone,String driverName,String carNumberPlate,Integer page, Integer pageSize){
        return mapper.getList(clientName,clientPhone,driverName,carNumberPlate,(page-1)*pageSize,pageSize);
    }
    /**
     * 查询：信息总数附带模糊查询
     */
    public Integer getListTotal(String clientName, String clientPhone,String driverName,String carNumberPlate){
        return mapper.getListTotal(clientName,clientPhone,driverName,carNumberPlate);
    }
    /**
     * 计算前一天的单日增量
     */
    public Integer getYesterdayIncrementForWaybillSnapshot(){
        Integer begin=BaseUtil.getCalendar0Time();
        return mapper.getYesterdayIncrementForWaybillSnapshot(begin,begin+86400);
    }
}
