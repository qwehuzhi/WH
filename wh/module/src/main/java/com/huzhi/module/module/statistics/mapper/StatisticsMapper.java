package com.huzhi.module.module.statistics.mapper;

import com.huzhi.module.module.enterprise.entity.Enterprise;
import com.huzhi.module.module.statistics.entity.Statistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jdk.nashorn.internal.runtime.JSONListAdapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 按天统计的数据表 Mapper 接口
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Mapper
public interface StatisticsMapper{
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    @Select("SELECT * FROM statistics WHERE id=#{id} and is_deleted = 0")
    Statistics getById(@Param(value = "id") BigInteger id);
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    @Select("SELECT * FROM statistics WHERE id=#{id}")
    Statistics extractById(@Param(value = "id") BigInteger id);
    /**
     * 插入信息
     * @return
     */
    Integer insert(@Param(value = "statistics") Statistics statistics);
    /**
     * 修改信息
     * @return
     */
    Integer update(@Param(value = "statistics") Statistics statistics);
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    @Update("UPDATE statistics SET is_deleted = 1,update_time = #{updateTime} WHERE Id=#{Id}")
    int delete(@Param(value = "id") BigInteger id,@Param(value = "updateTime") Integer updateTime);
    /**
     * 查询前一天的数据
     */
    Statistics getByTime(@Param(value = "timeYear")String timeYear,
                         @Param(value = "timeMonth")String timeMonth,
                         @Param(value = "timeDay")String timeDay);
    /**
     * 查询一段时间内所有数据
     */
    List<Statistics> getTotalByTime(@Param(value = "timeYear")String timeYear,
                                    @Param(value = "timeMonth")String timeMonth,
                                    @Param(value = "timeDay")String timeDay);
}
