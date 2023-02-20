package com.huzhi.module.utils;

import com.huzhi.module.module.user.entity.UserSign;
import com.alibaba.fastjson.JSON;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class SignUtil {
    private final static String SIGN_SALT = "_2019_dream";
    private final static int EXPIRATION_TIME = 1209600;
    private static final String PASSWORD_SALT = "__MLZ_RED";
    public static int getExpirationTime(){
        return EXPIRATION_TIME;
    }
    public static String makeSign(BigInteger userId) {
        UserSign sign = new UserSign();
        sign.setExpiration(TimeUtil.getNowTime() + EXPIRATION_TIME);
        sign.setSalt(SIGN_SALT);
        sign.setUserId(userId);
        byte[] rawSign = Base64.getEncoder().encode(JSON.toJSONString(sign).getBytes(StandardCharsets.UTF_8));
        return new String(rawSign, StandardCharsets.UTF_8).trim();
    }
    public static String marshal(String password) {
        return BaseUtil.md5(PASSWORD_SALT + password);
    }

    /**
     * 请求获取sign不为空时
     * @param sign
     * @return
     */
    public static BigInteger parseSign(String sign) {
        if (BaseUtil.isEmpty(sign)) {
            return null;
        }

        byte[] bytes = Base64.getDecoder().decode(sign.getBytes(StandardCharsets.UTF_8));
        String rawSign = new String(bytes, StandardCharsets.UTF_8);
        UserSign userSign = null;
        try {
            userSign = JSON.parseObject(rawSign, UserSign.class);
        } catch (Exception cause) {
            // ignores
        }
        if (userSign == null) {
            return null;
        }

        int time = userSign.getExpiration();
        int current = TimeUtil.getNowTime();
        if (current > time) {
            // a expired sign
            return null;
        }

        return userSign.getUserId();
    }
}
