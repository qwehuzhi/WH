package com.huzhi.console.controller.log;

import com.huzhi.console.annotations.VerifiedUser;
import com.huzhi.console.domain.log.LogListBaseVO;
import com.huzhi.console.domain.log.LogListVO;
import com.huzhi.module.module.log.entity.Log;
import com.huzhi.module.module.log.service.LogService;
import com.huzhi.module.module.user.entity.User;
import com.huzhi.module.response.Response;
import com.huzhi.module.utils.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class LogController {
    private final LogService logService;
    @Autowired
    public LogController(LogService logService){
        this.logService=logService;
    }
    private final Integer pageSize=5;
    @RequestMapping("/log")
    public Response log(@VerifiedUser User loginUser,
                        @RequestParam(value = "page")Integer page,
                        @RequestParam(value = "userId",required = false) BigInteger userId,
                        @RequestParam(value = "type",required = false)Integer type,
                        @RequestParam(value = "operate",required = false)String operate){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        operate=BaseUtil.isEmpty(operate)?null:operate.trim();
        List<Log> listLog = logService.getList(userId, type, operate, page, pageSize);
        List<LogListBaseVO> list=new ArrayList<>();
        for (Log l:listLog) {
            LogListBaseVO entity=new LogListBaseVO();
            entity.setUserId(l.getUserId());
            entity.setType(l.getType());
            entity.setOperateTime(BaseUtil.timeStamp2Date(l.getOperateTime()));
            entity.setOperate(l.getOperate());
            entity.setExtra(l.getExtra());
            list.add(entity);
        }
        LogListVO logListVO=new LogListVO();
        logListVO.setTotal(logService.getListTotal(userId, type, operate));
        logListVO.setPageSize(pageSize);
        logListVO.setList(list);
        return new Response(1001,logListVO);
    }
}
