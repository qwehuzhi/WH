package com.huzhi.console.controller.statistics;

import com.huzhi.console.annotations.VerifiedUser;
import com.huzhi.console.domain.statistical.*;
import com.huzhi.module.module.statistics.entity.Statistics;
import com.huzhi.module.module.statistics.service.StatisticsService;
import com.huzhi.module.module.user.entity.User;
import com.huzhi.module.response.Response;
import com.huzhi.module.utils.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.*;

@Slf4j
@RestController
public class StatisticsController {
    private final StatisticsService statisticsService;
    @Autowired
    public StatisticsController(StatisticsService statisticsService){
        this.statisticsService=statisticsService;
    }

    /**
     * 总量统计页
     * @param loginUser
     * @return
     */
    @RequestMapping("/statistical/day")
    public Response statisticalDay(@VerifiedUser User loginUser){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        Map<Integer,String> names=new HashMap<>();
        names.put(1,"业务量(已完成运单)");
        names.put(2,"客户数量");
        names.put(3,"已注册司机数量");
        names.put(4,"已注册车辆数量");
        Map<Integer, BigInteger[]> totals = statisticsService.getTotalForStatistics(1,2,3);
        Map<Integer, String[]> yearAndMouth = BaseUtil.getYearAndMouth(1, 2, 3);
        List<StatisticalDataVO> statisticalDataVOList=new ArrayList<>();
        for (int j=1;j<5;j++){
            StatisticalDataVO statisticalDataVO=new StatisticalDataVO();
            statisticalDataVO.setArrangeName(names.get(j));
            List<BaseStatisticalDataVO> baseStatisticalDataVOS=new ArrayList<>();
            BigInteger[] allTotal = totals.get(j);
            for(int i=0;i<3;i++){
                BaseStatisticalDataVO baseVO=new BaseStatisticalDataVO();
                baseVO.setTime(yearAndMouth.get(i+1)[0]+"-"+yearAndMouth.get(i+1)[1]);
                baseVO.setNumber(allTotal[i]);
                baseStatisticalDataVOS.add(baseVO);
            }
            statisticalDataVO.setStatisticalData(baseStatisticalDataVOS);
            statisticalDataVOList.add(statisticalDataVO);
        }
        DataDisplayVO dataDisplayVO=new DataDisplayVO();
        dataDisplayVO.setDataDisplay(statisticalDataVOList);
        return new Response(1001,dataDisplayVO);
    }

    /**
     * 增量统计页
     * @param loginUser
     * @return
     */
    @RequestMapping("/statistical/increment")
    public Response statisticalIncrement(@VerifiedUser User loginUser) {
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        Map<Integer, String[]> time = BaseUtil.getYearAndMouth(1, 2, 3);
        Map<Integer,String> names=new HashMap<>();
        names.put(1,"业务量(已完成运单)");
        names.put(2,"客户数量");
        names.put(3,"已注册司机数量");
        names.put(4,"已注册车辆数量");
        Map<Integer, BigInteger[]> oldTotals = statisticsService.getTotalForStatistics(13,14,15);
        Map<Integer, BigInteger[]> totals = statisticsService.getTotalForStatistics(1,2,3);
        Map<Integer, String[]> yearAndMouth = BaseUtil.getYearAndMouth(1, 2, 3);
        List<StatisticalDataVO> statisticalDataVOList=new ArrayList<>();
        for (int j=1;j<5;j++){
            StatisticalDataVO statisticalDataVO=new StatisticalDataVO();
            statisticalDataVO.setArrangeName(names.get(j));
            List<BaseStatisticalDataVO> baseStatisticalDataVOS=new ArrayList<>();
            BigInteger[] allTotal = totals.get(j);
            BigInteger[] oldAllTotal=oldTotals.get(j);
            for(int i=0;i<3;i++){
                BaseStatisticalDataVO baseVO=new BaseStatisticalDataVO();
                baseVO.setTime(yearAndMouth.get(i+1)[0]+"-"+yearAndMouth.get(i+1)[1]);
                baseVO.setNumber(allTotal[i].subtract(oldAllTotal[i]));
                baseStatisticalDataVOS.add(baseVO);
            }
            statisticalDataVO.setStatisticalData(baseStatisticalDataVOS);
            statisticalDataVOList.add(statisticalDataVO);
        }
        DataDisplayVO dataDisplayVO=new DataDisplayVO();
        dataDisplayVO.setDataDisplay(statisticalDataVOList);
        return new Response(1001,dataDisplayVO);
    }

    /**
     * 按日统计的运单数量
     */
    @RequestMapping("/waybill/day")
    public Response waybillDay(@VerifiedUser User loginUser,
                               @Param(value = "year")String year,
                               @Param(value = "month")String month,
                               @Param(value = "day")String day){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        year=year.trim();
        month=month.trim();
        day=day.trim();
        Statistics statistics = statisticsService.getByTime(year, month, day);
        TotalVO totalVO=new TotalVO();
        totalVO.setTotal(statistics.getWaybillIncrement());
        return new Response(1001,totalVO);
    }
    /**
     * 按日统计的客户数量
     */
    @RequestMapping("/client/day")
    public Response clientDay(@VerifiedUser User loginUser,
                               @Param(value = "year")String year,
                               @Param(value = "month")String month,
                               @Param(value = "day")String day){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        year=year.trim();
        month=month.trim();
        day=day.trim();
        Statistics statistics = statisticsService.getByTime(year, month, day);
        TotalVO totalVO=new TotalVO();
        totalVO.setTotal(statistics.getClientIncrement());
        return new Response(1001,totalVO);
    }
    /**
     * 按日统计的司机数量
     */
    @RequestMapping("/driver/day")
    public Response driverDay(@VerifiedUser User loginUser,
                              @Param(value = "year")String year,
                              @Param(value = "month")String month,
                              @Param(value = "day")String day){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        year=year.trim();
        month=month.trim();
        day=day.trim();
        Statistics statistics = statisticsService.getByTime(year, month, day);
        TotalVO totalVO=new TotalVO();
        totalVO.setTotal(statistics.getDriverIncrement());
        return new Response(1001,totalVO);
    }
    /**
     * 按日统计的车辆数量
     */
    @RequestMapping("/car/day")
    public Response carDay(@VerifiedUser User loginUser,
                              @Param(value = "year")String year,
                              @Param(value = "month")String month,
                              @Param(value = "day")String day){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        year=year.trim();
        month=month.trim();
        day=day.trim();
        Statistics statistics = statisticsService.getByTime(year, month, day);
        TotalVO totalVO=new TotalVO();
        totalVO.setTotal(statistics.getCarIncrement());
        return new Response(1001,totalVO);
    }
    /**
     * 按月统计的运单数量
     */
    @RequestMapping("/waybill/month")
    public Response waybillMonth(@VerifiedUser User loginUser,
                           @Param(value = "year")String year,
                           @Param(value = "month")String month){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        year=year.trim();
        month=month.trim();
        List<Statistics> serviceTotalByTime = statisticsService.getTotalByTime(year, month, null);
        BigInteger total=new BigInteger("0");
        for (Statistics s :
                serviceTotalByTime) {
            total=total.add(s.getWaybillIncrement());
        }
        TotalVO totalVO=new TotalVO();
        totalVO.setTotal(total);
        return new Response(1001,totalVO);
    }
    /**
     * 按月统计的客户数量
     */
    @RequestMapping("/client/month")
    public Response clientMonth(@VerifiedUser User loginUser,
                                 @Param(value = "year")String year,
                                 @Param(value = "month")String month){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        year=year.trim();
        month=month.trim();
        List<Statistics> serviceTotalByTime = statisticsService.getTotalByTime(year, month, null);
        BigInteger total=new BigInteger("0");
        for (Statistics s :
                serviceTotalByTime) {
            total=total.add(s.getClientIncrement());
        }
        TotalVO totalVO=new TotalVO();
        totalVO.setTotal(total);
        return new Response(1001,totalVO);
    }
    /**
     * 按月统计的司机数量
     */
    @RequestMapping("/driver/month")
    public Response driverMonth(@VerifiedUser User loginUser,
                                @Param(value = "year")String year,
                                @Param(value = "month")String month){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        year=year.trim();
        month=month.trim();
        List<Statistics> serviceTotalByTime = statisticsService.getTotalByTime(year, month, null);
        BigInteger total=new BigInteger("0");
        for (Statistics s :
                serviceTotalByTime) {
            total=total.add(s.getDriverIncrement());
        }
        TotalVO totalVO=new TotalVO();
        totalVO.setTotal(total);
        return new Response(1001,totalVO);
    }
    /**
     * 按月统计的车辆数量
     */
    @RequestMapping("/car/month")
    public Response carMonth(@VerifiedUser User loginUser,
                                @Param(value = "year")String year,
                                @Param(value = "month")String month){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        year=year.trim();
        month=month.trim();
        List<Statistics> serviceTotalByTime = statisticsService.getTotalByTime(year, month, null);
        BigInteger total=new BigInteger("0");
        for (Statistics s :
                serviceTotalByTime) {
            total=total.add(s.getCarIncrement());
        }
        TotalVO totalVO=new TotalVO();
        totalVO.setTotal(total);
        return new Response(1001,totalVO);
    }
    /**
     * 按年统计的运单数量
     */
    @RequestMapping("/waybill/year")
    public Response waybillYear(@VerifiedUser User loginUser,
                             @Param(value = "year")String year){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        year=year.trim();
        List<Statistics> serviceTotalByTime = statisticsService.getTotalByTime(year, null, null);
        BigInteger total=new BigInteger("0");
        for (Statistics s : serviceTotalByTime) {
            total=total.add(s.getWaybillIncrement());
        }
        TotalVO totalVO=new TotalVO();
        totalVO.setTotal(total);
        return new Response(1001,totalVO);
    }
    /**
     * 按年统计的客户数量
     */
    @RequestMapping("/client/year")
    public Response clientYear(@VerifiedUser User loginUser,
                                @Param(value = "year")String year){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        year=year.trim();
        List<Statistics> serviceTotalByTime = statisticsService.getTotalByTime(year, null, null);
        BigInteger total=new BigInteger("0");
        for (Statistics s : serviceTotalByTime) {
            total=total.add(s.getClientIncrement());
        }
        TotalVO totalVO=new TotalVO();
        totalVO.setTotal(total);
        return new Response(1001,totalVO);
    }

    /**
     * 按年统计的司机数量
     */
    @RequestMapping("/driver/year")
    public Response driverYear(@VerifiedUser User loginUser,
                               @Param(value = "year")String year){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        year=year.trim();
        List<Statistics> serviceTotalByTime = statisticsService.getTotalByTime(year, null, null);
        BigInteger total=new BigInteger("0");
        for (Statistics s : serviceTotalByTime) {
            total=total.add(s.getDriverIncrement());
        }
        TotalVO totalVO=new TotalVO();
        totalVO.setTotal(total);
        return new Response(1001,totalVO);
    }
    /**
     * 按年统计的车辆数量
     */
    @RequestMapping("/car/year")
    public Response carYear(@VerifiedUser User loginUser,
                               @Param(value = "year")String year){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        year=year.trim();
        List<Statistics> serviceTotalByTime = statisticsService.getTotalByTime(year, null, null);
        BigInteger total=new BigInteger("0");
        for (Statistics s : serviceTotalByTime) {
            total=total.add(s.getCarIncrement());
        }
        TotalVO totalVO=new TotalVO();
        totalVO.setTotal(total);
        return new Response(1001,totalVO);
    }
}
