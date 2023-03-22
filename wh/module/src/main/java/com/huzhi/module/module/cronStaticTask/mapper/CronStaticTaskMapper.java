package com.huzhi.module.module.cronStaticTask.mapper;

import com.huzhi.module.module.cronStaticTask.entity.CronStaticTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 定时任务表 Mapper 接口
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Mapper
public interface CronStaticTaskMapper {
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    @Select("SELECT * FROM cron_static_task WHERE id=#{id} and is_deleted = 0")
    CronStaticTask getById(@Param(value = "id") BigInteger id);
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    @Select("SELECT * FROM cron_static_task WHERE id=#{id}")
    CronStaticTask extractById(@Param(value = "id") BigInteger id);
    /**
     * 插入信息
     * @return
     */
    Integer insert(@Param(value = "cronStaticTask") CronStaticTask cronStaticTask);
    /**
     * 修改信息
     * @return
     */
    Integer update(@Param(value = "cronStaticTask") CronStaticTask cronStaticTask);
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    @Update("UPDATE cron_static_task SET is_deleted = 1,update_time = #{updateTime} WHERE Id=#{Id}")
    int delete(@Param(value = "id") BigInteger id,@Param(value = "updateTime") Integer updateTime);
    /**
     * 查询某天的记录
     */
    @Select("select * from cron_static_task where time_year = #{timeYear} and time_month = #{timeMonth} and time_day = #{timeDay}")
    CronStaticTask getByTime(@Param(value = "timeYear")String timeYear,
                             @Param(value = "timeMonth")String timeMonth,
                             @Param(value = "timeDay")String timeDay);
    /**
     * 修改任务状态
     */
    @Update("update cron_static_task set move_status=#{moveStatus},end_time=#{endTime}" +
            "where time_year=#{timeYear} and time_month=#{timeMonth} and time_day=#{timeDay} and" +
            "task=#{task}")
    Integer changeState(@Param(value = "moveStatus")Integer moveStatus,
                        @Param(value = "endTime")Integer endTime,
                        @Param(value = "timeYear")String timeYear,
                        @Param(value = "timeMonth")String timeMonth,
                        @Param(value = "timeDay")String timeDay,
                        @Param(value = "task")Integer task);
    /**
     * 定时任务列表
     */
    List<CronStaticTask> taskList(@Param(value = "offset")Integer offset,
                                  @Param(value = "pageSize") Integer pageSize,
                                  @Param(value = "task")String task,
                                  @Param(value = "year")String year,
                                  @Param(value = "month")String month,
                                  @Param(value = "day")String day);
    Integer taskListTotal(@Param(value = "task")String task,
                          @Param(value = "year")String year,
                          @Param(value = "month")String month,
                          @Param(value = "day")String day);
}
