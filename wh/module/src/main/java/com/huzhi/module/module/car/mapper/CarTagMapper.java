package com.huzhi.module.module.car.mapper;

import com.huzhi.module.module.car.entity.CarTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;

/**
 * <p>
 * 标签表 Mapper 接口
 * </p>
 *
 * @author huzhi
 * @since 2023-02-16
 */
@Mapper
public interface CarTagMapper{
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    @Select("SELECT * FROM car_tag WHERE id=#{id} and is_deleted = 0")
    CarTag getById(@Param(value = "id") BigInteger id);
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    @Select("SELECT * FROM car_tag WHERE id=#{id}")
    CarTag extractById(@Param(value = "id") BigInteger id);
    /**
     * 插入信息
     * @return
     */
    Integer insert(@Param(value = "carTag") CarTag carTag);
    /**
     * 修改信息
     * @return
     */
    Integer update(@Param(value = "carTag") CarTag carTag);
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    @Update("UPDATE car_tag SET is_deleted = 1,update_time = #{updateTime} WHERE Id=#{Id}")
    int delete(@Param(value = "id") BigInteger id,@Param(value = "updateTime") Integer updateTime);
    /**
     * 根据标签查询id
     */
    CarTag getByTag(@Param(value = "tag")String tag);
}
