package com.huzhi.module.code;

public enum TaskCode {
    RenovateStatistics(1,"Statistics表数据统计");
    private final Integer code;
    private final String msg;
    TaskCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public Integer getCode() {
        return code;
    }
    public static String getValueByCode(Integer code){
        for(TaskCode taskCode:TaskCode.values()){
            if(taskCode.getCode().equals(code)){
                return taskCode.getMsg();
            }
        }
        return  null;
    }
}
