package com.huzhi.module.code;

public enum OperateCode {
    WaybillInsert(1,"运单生成"),
    WaybillUpdate(2,"运单修改"),
    CarExamineSuccess(3,"车辆审核通过"),
    CarExamineFail(4,"车辆审核不通过"),
    DriverExamineSuccess(5,"司机审核通过"),
    DriverExamineFail(6,"司机审核不通过"),
    WaybillExamineSuccess(7,"运单审核通过"),
    WaybillExamineFail(8,"运单审核不通过");
    private final Integer id;
    private final String code;
    OperateCode(Integer id, String code) {
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
