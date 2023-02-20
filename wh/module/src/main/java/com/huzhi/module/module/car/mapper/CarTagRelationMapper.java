package com.huzhi.module.module.car.mapper;

import com.huzhi.module.module.car.entity.CarTagRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 车辆标签关系表 Mapper 接口
 * </p>
 *
 * @author huzhi
 * @since 2023-02-16
 */
@Mapper
public interface CarTagRelationMapper{
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    @Select("SELECT * FROM car_tag_relation WHERE id=#{id} and is_deleted = 0")
    CarTagRelation getById(@Param(value = "id") BigInteger id);
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    @Select("SELECT * FROM car_tag_relation WHERE id=#{id}")
    CarTagRelation extractById(@Param(value = "id") BigInteger id);
    /**
     * 插入信息
     * @return
     */
    Integer insert(@Param(value = "carTagRelation") CarTagRelation carTagRelation);
    /**
     * 修改信息
     * @return
     */
    Integer update(@Param(value = "carTagRelation") CarTagRelation carTagRelation);
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    @Update("UPDATE car_tag_relation SET is_deleted = 1,update_time = #{updateTime} WHERE Id=#{Id}")
    int delete(@Param(value = "id") BigInteger id,@Param(value = "updateTime") Integer updateTime);
    /**
     * 根据carId删除
     */
    int deleteByCarId(@Param(value = "carId")BigInteger carId,
                      @Param(value = "updateTime") Integer updateTime);
    /**
     * 根据tagId删除
     */
    int deleteByTagId(@Param(value = "tagId")BigInteger tagId,
                      @Param(value = "updateTime") Integer updateTime);
    /**
     * 根据carId或tagId取出多条数据
     * @param carId
     * @return
     */
    List<CarTagRelation> getByCarIdOrTagIdList(@Param(value = "carId")BigInteger carId,
                                                @Param(value = "tagId")BigInteger tagId);
    /**
     * 根据carId和tagId取出单条数据
     * @param carId
     * @return
     */
    CarTagRelation getByCarIdAndTagId(@Param(value = "carId")BigInteger carId,
                                     @Param(value = "tagId")BigInteger tagId);
}
