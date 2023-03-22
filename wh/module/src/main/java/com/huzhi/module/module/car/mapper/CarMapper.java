package com.huzhi.module.module.car.mapper;

import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.company.entity.Company;
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
                         @Param(value = "enterpriseId") String enterpriseId,
                         @Param(value = "offset")Integer offset,
                         @Param(value = "pageSize") Integer pageSize);
    /**
     * 查询车辆信息总数附带模糊查询
     * @return
     */
    Integer getCarsTotal(@Param(value = "numberPlate") String numberPlate,
                         @Param(value = "enterpriseId") String enterpriseId);
    /**
     * 审核
     */
    Integer examine(@Param(value = "id") BigInteger id,
                    @Param(value = "examineStatus") Integer examineStatus,
                    @Param(value = "examineRemarks") String examineRemarks,
                    @Param(value = "updateTime") Integer updateTime);
    /**
     * 通过车牌获取对应的id列表
     * 用于模糊查询
     * @return              id的列表
     */
    @Select("select id from car where name like concat('%',#{name},'%')")
    List<String> getIdByOption(@Param(value = "name") String name);
    /**
     * 通过多个id获取
     */
    @Select("select * from car where id in (${ids})")
    List<Car> getNameByIds(@Param(value = "ids") String ids);
    /**
     * 模糊搜索审核通过的车辆
     */
    @Select("select * from car where number_plate like concat('%',#{carNumberPlate},'%') and examine_status=1 and is_deleted=0")
    List<Car> getBySelect(@Param(value = "carNumberPlate")String carNumberPlate);
    /**
     * 计算前一天的单日增量
     */
    @Select("select count(*) from car where create_time > #{begin} and create_time < #{end}")
    Integer getYesterdayIncrementForCar(@Param(value = "begin") Integer begin,
                                        @Param(value = "end") Integer end);
}
