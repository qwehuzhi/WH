package com.huzhi.module.module.company.mapper;

import com.huzhi.module.module.client.entity.Client;
import com.huzhi.module.module.company.entity.Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 承运公司表 Mapper 接口
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Mapper
public interface CompanyMapper{
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    @Select("SELECT * FROM company WHERE id=#{id} and is_deleted = 0")
    Company getById(@Param(value = "id") BigInteger id);
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    @Select("SELECT * FROM company WHERE id=#{id}")
    Company extractById(@Param(value = "id") BigInteger id);
    /**
     * 插入信息
     * @return
     */
    Integer insert(@Param(value = "company") Company company);
    /**
     * 修改信息
     * @return
     */
    Integer update(@Param(value = "company") Company company);
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    @Update("UPDATE company SET is_deleted = 1,update_time = #{updateTime} WHERE Id=#{Id}")
    Integer delete(@Param(value = "id") BigInteger id,@Param(value = "updateTime") Integer updateTime);
    /**
     * 查询：列表显示所有信息并分页
     */
    List<Company> getList(@Param(value = "name")String name,
                         @Param(value = "phone") String phone,
                         @Param(value = "offset")int offset,
                         @Param(value = "pageSize") int pageSize);
    /**
     * 查询：信息总数附带模糊查询
     */
    Integer getListTotal(@Param(value = "name") String name,
                         @Param(value = "phone") String phone);
    /**
     * 通过公司名称获取对应的id列表
     * 用于模糊查询
     * @return              id的列表
     */
    @Select("select id from company where name like concat('%',#{name},'%')")
    List<String> getIdByOption(@Param(value = "name") String name);
    /**
     * 通过多个id获取名称
     */
    @Select("select * from company where id in (${ids})")
    List<Company> getNameByIds(@Param(value = "ids")String ids);
    /**
     * 模糊搜索公司表
     */
    @Select("select * from company where name like concat('%',#{name},'%') and is_deleted=0")
    List<Company> getBySelect(@Param(value = "name")String name);
}
