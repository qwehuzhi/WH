package com.huzhi.console.controller.cronStaticTask;

import com.huzhi.console.annotations.VerifiedUser;
import com.huzhi.console.domain.cronStaticTask.TaskListBaseVO;
import com.huzhi.console.domain.cronStaticTask.TaskListVO;
import com.huzhi.module.code.TaskCode;
import com.huzhi.module.code.TaskMoveCode;
import com.huzhi.module.module.statistics.service.BaseStatisticsService;
import com.huzhi.module.module.cronStaticTask.entity.CronStaticTask;
import com.huzhi.module.module.cronStaticTask.service.CronStaticTaskService;
import com.huzhi.module.module.user.entity.User;
import com.huzhi.module.response.Response;
import com.huzhi.module.utils.BaseUtil;
import com.huzhi.module.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CronStaticTaskController {
    private final int pageSize=5;
    private final int utmost=50;//最多尝试执行插入次数
    @Autowired
    private BaseStatisticsService baseStatisticsService;
    @Autowired
    private CronStaticTaskService cronStaticTaskService;
    @Scheduled(cron = "0 10 0 * * ?")
    public void scheduledTasks(){
        Map<Integer,String> time = BaseUtil.getCalendar();
        for(int i=0;i<utmost;i++){
            CronStaticTask back = cronStaticTaskService.getByTime(time.get(0),time.get(1),time.get(2));
            if(BaseUtil.isEmpty(back)){
                if(cronStaticTaskService.taskInsert(TaskCode.RenovateStatistics.getCode())){
                    BigInteger id = baseStatisticsService.renovateStatistical();
                    cronStaticTaskService.taskEnd(!BaseUtil.isEmpty(id),TaskCode.RenovateStatistics.getCode());
                }
            }
        }
        CronStaticTask byTime = cronStaticTaskService.getByTime(time.get(0),time.get(1),time.get(2));
        if(BaseUtil.isEmpty(byTime)){
            log.info("主机ip:"+ IpUtil.getLocalIpAddress()+",statistics表定时任务创建失败");
        }
    }
    @RequestMapping("/task/list")
    public Response taskList(@VerifiedUser User loginUser,
                             @RequestParam(value = "page")Integer page,
                             @RequestParam(value = "task",required = false)String task,
                             @RequestParam(value = "year",required = false)String year,
                             @RequestParam(value = "month",required = false)String month,
                             @RequestParam(value = "day",required = false)String day){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        List<CronStaticTask> cronStaticTasks = cronStaticTaskService.taskList(page, pageSize, task, year, month, day);
        List<TaskListBaseVO> list=new ArrayList<>();
        for (CronStaticTask c :
                cronStaticTasks) {
            TaskListBaseVO entity=new TaskListBaseVO();
            entity.setId(c.getId());
            entity.setIp(c.getIp());
            entity.setTask(TaskCode.getValueByCode(c.getTask()));
            entity.setTime(c.getTimeYear()+"-"+c.getTimeMonth()+"-"+c.getTimeDay());
            entity.setMoveStatus(TaskMoveCode.getValueByCode(c.getMoveStatus()));
            entity.setStartTime(BaseUtil.timeStamp2Date(c.getStartTime()));
            entity.setEndTime(BaseUtil.timeStamp2Date(c.getEndTime()));
            entity.setCreateTime(BaseUtil.timeStamp2Date(c.getCreateTime()));
            entity.setUpdateTime(BaseUtil.timeStamp2Date(c.getUpdateTime()));
            entity.setRemarks(c.getRemarks());
            list.add(entity);
        }
        TaskListVO taskListVO=new TaskListVO();
        taskListVO.setPageSize(pageSize);
        taskListVO.setTotal(cronStaticTaskService.taskListTotal(task,year,month,day));
        taskListVO.setList(list);
        return new Response(1001,taskListVO);
    }
}
