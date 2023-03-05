package com.huzhi.module.module.car.mapper;

import com.huzhi.module.module.car.entity.Car;
import org.apache.ibatis.annotations.*;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface CarMapper {
    /**
     * 根据查询（未删除）车辆数据
     * @return
     */
    @Select("SELECT * FROM car WHERE id=#{id} and is_deleted = 0")
    Car getById(@Param(value = "id") BigInteger id);
    /**
     * 根据id取出车辆数据
     * @return
     */
    @Select("SELECT * FROM car WHERE id=#{id}")
    Car extractById(@Param(value = "id") BigInteger id);
    /**
     * 插入车辆信息
     * @return
     */
    Integer insert(@Param(value = "car") Car car);
    /**
     * 修改车辆信息
     * @return
     */
    Integer update(@Param(value = "car") Car car);
    /**
     *
     * 删除车辆信息（逻辑删除）
     * @return
     */
    @Update("update car set is_deleted=1,update_time=#{updateTime} where id=#{id}")
    int delete(@Param(value = "id") BigInteger id,
               @Param(value = "updateTime") Integer updateTime);

    /**
     * 列表显示所有车辆信息并分页
     * 可以使用@ResultMap("EntityAll")注解设置数据库字段名和entity字段名的对应关系
     * 注意这里的EntityAll已经在xml文件中定义过，也可以在mapper接口中定义
     * 用法查询ResultMap
     * @return
     */
    List<Car> getCarList(@Param(value = "numberPlate")String numberPlate,
                         @Param(value = "carEnterpriseId") String carEnterpriseId,
                         @Param(value = "offset")int offset,
                         @Param(value = "pageSize") int pageSize);
    /**
     * 查询车辆信息总数（删除未删除都有）附带模糊查询
     * @return
     */
    Integer getCarsTotal(@Param(value = "numberPlate") String numberPlate,
                         @Param(value = "carEnterpriseId") String carEnterpriseId);
}
