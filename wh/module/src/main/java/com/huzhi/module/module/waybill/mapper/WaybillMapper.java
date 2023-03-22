package com.huzhi.module.module.waybill.mapper;

import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.enterprise.entity.Enterprise;
import com.huzhi.module.module.waybill.entity.Waybill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 运单信息表 Mapper 接口
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Mapper
public interface WaybillMapper{
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    @Select("SELECT * FROM waybill WHERE id=#{id} and is_deleted = 0")
    Waybill getById(@Param(value = "id") BigInteger id);
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    @Select("SELECT * FROM waybill WHERE id=#{id}")
    Waybill extractById(@Param(value = "id") BigInteger id);
    /**
     * 插入信息
     * @return
     */
    Integer insert(@Param(value = "waybill") Waybill waybill);
    /**
     * 修改信息
     * @return
     */
    Integer update(@Param(value = "waybill") Waybill waybill);
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    @Update("UPDATE waybill SET is_deleted = 1,update_time = #{updateTime} WHERE Id=#{Id}")
    int delete(@Param(value = "id") BigInteger id,@Param(value = "updateTime") Integer updateTime);
    /**
     * 列表显示所有运单信息并分页，附带模糊查询
     * @return
     */
    List<Waybill> getList(@Param(value = "companyIds")String companyIds,
                          @Param(value = "clientIds") String clientIds,
                          @Param(value = "driverIds") String driverIds,
                          @Param(value = "carIds") String carIds,
                          @Param(value = "offset")int offset,
                          @Param(value = "pageSize") int pageSize,
                          @Param(value = "examineStatus")String examineStatus,
                          @Param(value = "idDeleted")Integer idDeleted);
    /**
     * 查询运单总数附带模糊查询
     * @return
     */
    Integer getTotal(@Param(value = "companyIds")String companyIds,
                     @Param(value = "clientIds") String clientIds,
                     @Param(value = "driverIds") String driverIds,
                     @Param(value = "carIds") String carIds,
                     @Param(value = "examineStatus")String examineStatus,
                     @Param(value = "idDeleted")Integer idDeleted);
    /**
     * 审核
     */
    Integer examine(@Param(value = "id")BigInteger id,
                    @Param(value = "examineStatus") Integer examineStatus,
                    @Param(value = "examineRemarks") String examineRemarks,
                    @Param(value = "updateTime")Integer updateTime);
    /**
     * 时间校验
     */
    List<BigInteger> timeExamine(@Param(value = "deliveryTime") Integer deliveryTime,
                                 @Param(value = "arrivalTime") Integer arrivalTime,
                                 @Param(value = "carId") BigInteger carId,
                                 @Param(value = "driverId") BigInteger driverId);
}
