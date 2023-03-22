package com.huzhi.module.module.enterprise.mapper;

import com.huzhi.module.module.client.entity.Client;
import com.huzhi.module.module.enterprise.entity.Enterprise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 公司信息表 Mapper 接口
 * </p>
 *extends BaseMapper<Enterprise>
 * @author huzhi
 * @since 2023-02-03
 */
@Mapper
public interface EnterpriseMapper {
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    @Select("SELECT * FROM enterprise WHERE id=#{id} and is_deleted = 0")
    Enterprise getById(@Param(value = "id") BigInteger id);
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    @Select("SELECT * FROM enterprise WHERE id=#{id}")
    Enterprise extractById(@Param(value = "id") BigInteger id);
    /**
     * 插入信息
     * @return
     */
    Integer insert(@Param(value = "enterprise") Enterprise enterprise);
    /**
     * 修改信息
     * @return
     */
    Integer update(@Param(value = "enterprise") Enterprise enterprise);
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    @Update("UPDATE enterprise SET is_deleted = 1,update_time = #{updateTime} WHERE Id=#{Id}")
    Integer delete(@Param(value = "id") BigInteger id,@Param(value = "updateTime") Integer updateTime);
    /**
     * 通过公司名称获取对应的id列表
     * 用于模糊查询
     * @return              id的列表
     */
    @Select("select id from enterprise where name like concat('%',#{enterpriseName},'%')")
    List<String> getIdByOption(String enterpriseName);
    /**
     * 根据多个id查询（未删除）数据
     * @return
     */
    @Select("SELECT * FROM enterprise WHERE is_deleted = 0 and id in (${enterpriseId})")
    List<Enterprise> getByIdList(@Param(value = "enterpriseId") String enterpriseId);

    /**
     * 查询：列表显示所有信息并分页
     */
    List<Enterprise> getList(@Param(value = "name")String name,
                         @Param(value = "phone") String phone,
                         @Param(value = "offset")int offset,
                         @Param(value = "pageSize") int pageSize);
    /**
     * 查询：信息总数附带模糊查询
     */
    Integer getListTotal(@Param(value = "name") String name,
                         @Param(value = "phone") String phone);
}
