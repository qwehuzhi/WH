package com.huzhi.console.configuration;

import com.huzhi.module.utils.BaseUtil;
import com.huzhi.module.utils.SpringUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@Component
public class UserLoginInterceptor implements HandlerInterceptor {
    /**
     * 在请求处理之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        return true;
        /**
        String isAppS = SpringUtil.getProperty("application.isapp");
        boolean isConsole = isAppS.equals("1") ? false : true;
        if(isConsole){
            try {
                HttpSession session = request.getSession(false);
                if(BaseUtil.isEmpty(session)){
                    response.sendRedirect("http://localhost:8081/user/login/web");
                    return false;
                }
                //统一拦截（查询当前session是否存在user）(这里user会在每次登录成功后，写入session)
                String signKey = SpringUtil.getProperty("application.session.key");
                Object accountNumber=session.getAttribute(signKey);
                if (accountNumber != null) {//可以考虑用user里面的时间戳搞事情
                    return true;
                }
                response.sendRedirect("http://localhost:8081/car/list?page=1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
         */
        //如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        //如果设置为true时，请求将会继续执行后面的操作
    }
}
