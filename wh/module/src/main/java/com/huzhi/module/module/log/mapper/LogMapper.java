package com.huzhi.module.module.log.mapper;

import com.huzhi.module.module.client.entity.Client;
import com.huzhi.module.module.enterprise.entity.Enterprise;
import com.huzhi.module.module.log.entity.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 日志表 Mapper 接口
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Mapper
public interface LogMapper{
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    @Select("SELECT * FROM log WHERE id=#{id} and is_deleted = 0")
    Log getById(@Param(value = "id") BigInteger id);
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    @Select("SELECT * FROM log WHERE id=#{id}")
    Log extractById(@Param(value = "id") BigInteger id);
    /**
     * 插入信息
     * @return
     */
    Integer insert(@Param(value = "log") Log log);
    /**
     * 修改信息
     * @return
     */
    Integer update(@Param(value = "log") Log log);
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    @Update("UPDATE enterprise SET is_deleted = 1,update_time = #{updateTime} WHERE Id=#{Id}")
    int delete(@Param(value = "id") BigInteger id,@Param(value = "updateTime") Integer updateTime);
    /**
     * 查询：列表显示所有信息并分页
     */
    List<Log> getList(@Param(value = "userId")BigInteger userId,
                         @Param(value = "type") Integer type,
                         @Param(value = "operate")String operate,
                         @Param(value = "offset")int offset,
                         @Param(value = "pageSize") int pageSize);
    /**
     * 查询：信息总数附带模糊查询
     */
    Integer getListTotal(@Param(value = "userId")BigInteger userId,
                         @Param(value = "type") Integer type,
                         @Param(value = "operate")String operate);
}
