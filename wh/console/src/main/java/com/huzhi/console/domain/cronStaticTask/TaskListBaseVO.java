package com.huzhi.console.domain.cronStaticTask;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;


@Data
@Accessors(chain= true)
public class TaskListBaseVO {
        private BigInteger id;
        private String ip;
        private String task;
        private String time;
        private String moveStatus;
        private String startTime;
        private String endTime;
        private String createTime;
        private String updateTime;
        private String remarks;

}
