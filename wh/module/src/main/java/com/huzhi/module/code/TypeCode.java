package com.huzhi.module.code;

public enum TypeCode {
    Car(1,"车辆"),
    Driver(2,"司机"),
    Waybill(3,"运单");
    private final Integer id;
    private final String code;
    TypeCode(Integer id, String code) {
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
