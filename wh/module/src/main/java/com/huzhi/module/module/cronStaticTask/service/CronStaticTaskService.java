package com.huzhi.module.module.cronStaticTask.service;

import com.huzhi.module.code.TaskMoveCode;
import com.huzhi.module.module.cronStaticTask.entity.CronStaticTask;
import com.huzhi.module.module.cronStaticTask.mapper.CronStaticTaskMapper;
import com.huzhi.module.utils.BaseUtil;
import com.huzhi.module.utils.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 定时任务表 服务类
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Service
public class CronStaticTaskService {
    private CronStaticTaskMapper mapper;
    @Autowired
    public CronStaticTaskService(CronStaticTaskMapper mapper){
        this.mapper=mapper;
    }
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    public CronStaticTask getById(BigInteger id){
        return mapper.getById(id);
    }
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    public CronStaticTask extractById(BigInteger id){
        return mapper.extractById(id);
    }
    /**
     * 插入信息
     * @return
     */
    public Integer insert(CronStaticTask timeTask){
        return mapper.insert(timeTask);
    }
    /**
     * 修改信息
     * @return
     */
    public Integer update(CronStaticTask timeTask){
        return mapper.update(timeTask);
    }
    /**
     * 插入,修改信息
     * @return
     */
    public BigInteger editTimeTask(BigInteger id,String ip,Integer task,String timeYear,String timeMonth,String timeDay,
                             String remarks,Integer moveStatus,Integer startTime,Integer endTime){
        CronStaticTask entity=new CronStaticTask();
        entity.setIp(ip);
        entity.setTask(task);
        entity.setTimeYear(timeYear);
        entity.setTimeMonth(timeMonth);
        entity.setTimeDay(timeDay);
        entity.setRemarks(remarks);
        entity.setMoveStatus(moveStatus);
        entity.setStartTime(startTime);
        entity.setEndTime(endTime);
        if(BaseUtil.isEmpty(id)){
            entity.setCreateTime(BaseUtil.currentSeconds());
            return BigInteger.valueOf(insert(entity));
        }else {
            entity.setId(id);
            update(entity);
            return id;
        }
    }
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    public int delete(BigInteger id){
        return mapper.delete(id,BaseUtil.currentSeconds());
    }
    /**
     * 查询某天的记录
     */
    public CronStaticTask getByTime(String timeYear, String timeMonth, String timeDay){
        return mapper.getByTime(timeYear,timeMonth,timeDay);
    }
    /**
     * 根据ymd和任务名称，次数修改任务状态
     */
    public Integer changeState(Integer moveStatus,Integer endTime,String timeYear,String timeMonth,
                               String timeDay,Integer task){
        return mapper.changeState(moveStatus,endTime,timeYear,timeMonth,timeDay,task);
    }
    /**
     *  定时任务插入
     */
    public boolean taskInsert(Integer task){
        Map<Integer,String> time = BaseUtil.getCalendar();
        BigInteger back = editTimeTask(null, IpUtil.getLocalIpAddress(), task, time.get(0),time.get(1),time.get(2), null, TaskMoveCode.New.getCode(), BaseUtil.currentSeconds(), null);
        if(BaseUtil.isEmpty(back)){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 根据传入值在日志表记录状态
     */
    public void taskEnd(Boolean result,Integer task){
        Map<Integer,String> time = BaseUtil.getCalendar();
        if(result){
            changeState(TaskMoveCode.Success.getCode(),BaseUtil.currentSeconds(),
                    time.get(0),time.get(1),time.get(2),task);
        }else {
            changeState(TaskMoveCode.Fail.getCode(), BaseUtil.currentSeconds(),
                    time.get(0),time.get(1),time.get(2),task);
        }
    }
    /**
     * 定时任务列表
     */
    public List<CronStaticTask> taskList(Integer page, Integer pageSize,String task,String year,String month,String day){
        return mapper.taskList((page-1)*pageSize,pageSize,task,year,month,day);
    }
    public Integer taskListTotal(String task,String year,String month,String day){
        Integer back = mapper.taskListTotal(task, year, month, day);
        if(BaseUtil.isEmpty(back)){
            back=0;
        }
        return back;
    }
}
