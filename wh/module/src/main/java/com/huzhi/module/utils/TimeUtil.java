package com.huzhi.module.utils;

import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public abstract class TimeUtil {
    /**
     * 将数据库内时间戳转换成String时间格式
     * @return
     */
    private TimeUtil(){
        throw new Error("dot instantiation utils");
    }
    public static String timeConvert(long time){
        String result1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time * 1000));
        return result1;
    }
    static int nowTime = (int)(System.currentTimeMillis()/1000);
    public static int getNowTime() {
        return nowTime;
    }
}
