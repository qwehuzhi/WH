package com.huzhi.module.module.driver.mapper;

import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.driver.entity.Driver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 司机信息表 Mapper 接口
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Mapper
public interface DriverMapper{
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    @Select("SELECT * FROM driver WHERE id=#{id} and is_deleted = 0")
    Driver getById(@Param(value = "id") BigInteger id);
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    @Select("SELECT * FROM driver WHERE id=#{id}")
    Driver extractById(@Param(value = "id") BigInteger id);
    /**
     * 插入信息
     * @return
     */
    Integer insert(@Param(value = "driver") Driver driver);
    /**
     * 修改信息
     * @return
     */
    Integer update(@Param(value = "driver") Driver driver);
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    @Update("UPDATE driver SET is_deleted = 1,update_time = #{updateTime} WHERE Id=#{Id}")
    int delete(@Param(value = "id") BigInteger id,@Param(value = "updateTime") Integer updateTime);
    /**
     * 司机列表模糊查询
     */
    List<Driver> getList(@Param(value = "name") String name,
                         @Param(value = "phone") String phone,
                         @Param(value = "offset")int offset,
                         @Param(value = "pageSize") int pageSize);
    /**
     * 司机列表总数查询
     */
    Integer getTotal(@Param(value = "name") String name,
                     @Param(value = "phone") String phone);
    /**
     * 审核
     */
    Integer examine(@Param(value = "id") BigInteger id,
                    @Param(value = "examineStatus") int examineStatus,
                    @Param(value = "examineRemarks") String examineRemarks,
                    @Param(value = "updateTime") Integer updateTime);
    /**
     * 通过司机姓名获取对应的id列表
     * 用于模糊查询
     * @return              id的列表
     */
    @Select("select id from driver where name like concat('%',#{name},'%')")
    List<String> getIdByOption(@Param(value = "name")String name);
    /**
     * 通过多个id获取名称
     */
    @Select("select * from driver where id in (${ids})")
    List<Driver> getNameByIds(@Param(value = "ids")String ids);
    /**
     * 模糊搜索审核通过的司机
     */
    @Select("select * from driver where name like concat('%',#{name},'%') and phone like ('%',#{phone},'%') and examine_status=1 and is_deleted=0")
    List<Driver> getBySelect(@Param(value = "name")String name,
                             @Param(value = "phone")String phone);
    /**
     * 计算前一天的单日增量
     */
    @Select("select count(*) from driver where create_time > #{begin} and create_time < #{end}")
    Integer getYesterdayIncrementForDriver(@Param(value = "begin") Integer begin,
                                        @Param(value = "end") Integer end);
}
