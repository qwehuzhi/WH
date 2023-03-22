package com.huzhi.module.code;

public enum WaybillExamineCode {
    ExamineNew(0,"未审核状态(默认)"),
    ExamineSuccess(1,"审核成功"),
    ExamineFail(3,"审核不通过");
    private final Integer id;
    private final String code;
    WaybillExamineCode(Integer id, String code) {
        this.id = id;
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public Integer getId() {
        return id;
    }
}
