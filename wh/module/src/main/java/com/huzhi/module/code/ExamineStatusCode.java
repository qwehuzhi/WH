package com.huzhi.module.code;

public enum ExamineStatusCode {
    CarExamineSuccess(1,"审核成功"),
    CarExamineFail(0,"审核不通过"),
    DriverExamineSuccess(1,"司机审核成功"),
    DriverExamineFail(0,"司机审核不通过");
    private final Integer code;
    private final String msg;
    ExamineStatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public Integer getCode() {
        return code;
    }
}
