package com.huzhi.module.response;

import java.util.HashMap;
import java.util.Map;

public class ResponseCode {
    private static final Map<Integer, String> statusMap = new HashMap<Integer, String>();
    static {
        statusMap.put(1001,"OK");
        statusMap.put(1002,"没有登录哦~");
        statusMap.put(1003,"数据库新增和修改失败");
        statusMap.put(1004,"图片上传失败");
        statusMap.put(1005,"密码错误");
        statusMap.put(1006,"session不存在");
        statusMap.put(1010,"账号密码不匹配或账号不存在");

        statusMap.put(2001,"手机号并不存在");

        statusMap.put(4003,"权限不足");
        statusMap.put(4004,"链接超时");
    }
    public static String getMsg(Integer code) {
        return statusMap.get(code);
    }
}
