package com.huzhi.module.code;

/**
 * 任务运行状态
 */
public enum TaskMoveCode {
    New(0,"未运行"),
    Running(1,"进行中"),
    Success(2,"成功"),
    Fail(3,"失败");
    private final Integer code;
    private final String msg;
    TaskMoveCode(Integer code, String msg) {
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
        for(TaskMoveCode taskMoveCode:TaskMoveCode.values()){
            if(taskMoveCode.getCode().equals(code)){
                return taskMoveCode.getMsg();
            }
        }
        return  null;
    }
}
