package com.huzhi.module.code;

import java.util.HashMap;
import java.util.Map;

public class ResponseCode {
    private static final Map<Integer, String> statusMap = new HashMap<Integer, String>();
    static {
        statusMap.put(1001,"OK");
        statusMap.put(1002,"没有登录哦~");
        statusMap.put(1010,"账号密码不匹配或账号不存在");
        statusMap.put(1003,"手机号并不存在");

        statusMap.put(2002,"console新增数据异常");
        statusMap.put(2003,"console修改数据异常");
        statusMap.put(2004,"console删除数据异常");
        statusMap.put(2005,"console列表查询异常");
        statusMap.put(2006,"console图片上传失败");
        statusMap.put(2011,"console审核失败");

        statusMap.put(1005,"密码错误");
        statusMap.put(1006,"session不存在");
        statusMap.put(1012,"参数错误");



        statusMap.put(4003,"权限不足");
        statusMap.put(4004,"链接超时");
    }
    public static String getMsg(Integer code) {
        return statusMap.get(code);
    }
}
