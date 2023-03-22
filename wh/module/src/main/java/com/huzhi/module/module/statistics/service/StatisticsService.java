package com.huzhi.module.module.statistics.service;

import com.huzhi.module.module.statistics.entity.Statistics;
import com.huzhi.module.module.statistics.mapper.StatisticsMapper;
import com.huzhi.module.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 按天统计的数据表 服务类
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Service
public class StatisticsService{
    private final StatisticsMapper mapper;
    @Autowired
    public StatisticsService(StatisticsMapper mapper){
        this.mapper=mapper;
    }
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    public Statistics getById(BigInteger id){
        return mapper.getById(id);
    }
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    public Statistics extractById(BigInteger id){
        return mapper.extractById(id);
    }
    /**
     * 插入
     */
    public Integer insert(Statistics statistics){
        return mapper.insert(statistics);
    }
    /**
     * 修改
     */
    public Integer update(Statistics statistics){
        return mapper.update(statistics);
    }
    /**
     * 插入信息
     *
     * @return
     */
    public BigInteger editForStatistics(BigInteger id, BigInteger waybillIncrement, BigInteger waybillTotal, BigInteger clientIncrement,
                          BigInteger clientTotal, BigInteger carIncrement, BigInteger carTotal, BigInteger driverIncrement,
                          BigInteger driverTotal, String timeYear, String timeMonth, String timeDay, Integer isDeleted){
        Statistics entity=new Statistics();
        entity.setWaybillIncrement(waybillIncrement);
        entity.setWaybillTotal(waybillTotal);
        entity.setClientIncrement(clientIncrement);
        entity.setClientTotal(clientTotal);
        entity.setCarIncrement(carIncrement);
        entity.setCarTotal(carTotal);
        entity.setDriverIncrement(driverIncrement);
        entity.setDriverTotal(driverTotal);
        entity.setTimeYear(timeYear);
        entity.setTimeMonth(timeMonth);
        entity.setTimeDay(timeDay);
        entity.setIsDeleted(isDeleted);
        if(BaseUtil.isEmpty(id)){
            entity.setCreateTime(BaseUtil.currentSeconds());
            Integer back=insert(entity);
            return BigInteger.valueOf(back);
        }else {
            entity.setId(id);
            update(entity);
            return id;
        }
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
     * 查询前一天的数据
     */
    public Statistics getByTime(String timeYear,String timeMonth,String timeDay){
        return mapper.getByTime(timeYear,timeMonth,timeDay);
    }
    /**
     * 查询指定时间段内的所有记录
     */
    public List<Statistics> getTotalByTime(String timeYear,String timeMonth,String timeDay){
        return mapper.getTotalByTime(timeYear,timeMonth,timeDay);
    }
    /**
     * 统计运单的前3个月数据
     */
    public Map<Integer,BigInteger[]> getTotalForStatistics(Integer lastOneMonth,Integer lastTwoMonth,Integer lastThreeMonth) {
        BigInteger[] waybillTotal = {BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(0)};
        BigInteger[] clientTotal = {BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(0)};
        BigInteger[] driverTotal ={BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(0)};
        BigInteger[] carTotal = {BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(0)};
        Map<Integer, String[]> time = BaseUtil.getYearAndMouth(lastOneMonth, lastTwoMonth, lastThreeMonth);
        List<Statistics> totalOne = getTotalByTime(time.get(lastOneMonth)[0], time.get(lastOneMonth)[1], null);
        List<Statistics> totalTwo = getTotalByTime(time.get(lastTwoMonth)[0], time.get(lastTwoMonth)[1], null);
        List<Statistics> totalThree = getTotalByTime(time.get(lastThreeMonth)[0], time.get(lastThreeMonth)[1], null);
        List<List<Statistics>> totals = new ArrayList<>();
        totals.add(totalOne);
        totals.add(totalTwo);
        totals.add(totalThree);
        for (int i = 0; i < 3; i++) {
            for (List<Statistics> l : totals) {
                for (Statistics s : l) {
                    waybillTotal[i]=waybillTotal[i].add(s.getWaybillIncrement());
                    clientTotal[i]=clientTotal[i].add(s.getClientIncrement());
                    driverTotal[i]=driverTotal[i].add(s.getDriverIncrement());
                    carTotal[i]=carTotal[i].add(s.getCarIncrement());
                }
            }
        }
        Map<Integer, BigInteger[]> total = new HashMap<>();
        total.put(1, waybillTotal);
        total.put(2, clientTotal);
        total.put(3, driverTotal);
        total.put(4, carTotal);
        return total;
    }
}
