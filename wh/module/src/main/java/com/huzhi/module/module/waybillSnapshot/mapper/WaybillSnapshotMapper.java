package com.huzhi.module.module.waybillSnapshot.mapper;

import com.huzhi.module.module.company.entity.Company;
import com.huzhi.module.module.waybillSnapshot.entity.WaybillSnapshot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 运单快照表 Mapper 接口
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Mapper
public interface WaybillSnapshotMapper{
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    @Select("SELECT * FROM waybill_snapshot WHERE id=#{id} and is_deleted = 0")
    WaybillSnapshot getById(@Param(value = "id") BigInteger id);
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    @Select("SELECT * FROM waybill_snapshot WHERE id=#{id}")
    WaybillSnapshot extractById(@Param(value = "id") BigInteger id);
    /**
     * 插入信息
     * @return
     */
    Integer insert(@Param(value = "waybillSnapshot") WaybillSnapshot waybillSnapshot);
    /**
     * 修改信息
     * @return
     */
    Integer update(@Param(value = "waybillSnapshot") WaybillSnapshot waybillSnapshot);
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    @Update("UPDATE waybill_snapshot SET is_deleted = 1,update_time = #{updateTime} WHERE Id=#{Id}")
    Integer delete(@Param(value = "id") BigInteger id,@Param(value = "updateTime") Integer updateTime);
    /**
     * 查询：列表显示所有信息并分页
     */
    List<WaybillSnapshot> getList(@Param(value = "clientName")String clientName,
                                  @Param(value = "clientPhone") String clientPhone,
                                  @Param(value = "driverName")String driverName,
                                  @Param(value = "carNumberPlate")String carNumberPlate,
                                  @Param(value = "offset")int offset,
                                  @Param(value = "pageSize") int pageSize);
    /**
     * 查询：信息总数附带模糊查询
     */
    Integer getListTotal(@Param(value = "clientName")String clientName,
                         @Param(value = "clientPhone") String clientPhone,
                         @Param(value = "driverName")String driverName,
                         @Param(value = "carNumberPlate")String carNumberPlate);
    /**
     * 计算前一天的单日增量
     */
    @Select("select count(*) from waybill_snapshot where create_time > #{begin} and create_time < #{end}")
    Integer getYesterdayIncrementForWaybillSnapshot(@Param(value = "begin") Integer begin,
                                                    @Param(value = "end") Integer end);
}
