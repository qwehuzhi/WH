package com.huzhi.console;

import com.alibaba.fastjson.JSON;
import com.huzhi.console.annotations.VerifiedUser;
import com.huzhi.module.module.user.entity.User;
import com.huzhi.module.module.user.service.BaseUserService;
import com.huzhi.module.utils.BaseUtil;
import com.huzhi.module.utils.SignUtil;
import com.huzhi.module.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;

/**
 * 注释
 */
@Slf4j
public class UserAuthorityResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private BaseUserService userService;
    private boolean isCheckAuthority;

    public UserAuthorityResolver(ApplicationArguments appArguments) {
        String[] arguments = appArguments.getSourceArgs();
        if (arguments == null || arguments.length <= 3) {
            isCheckAuthority = true;
            return ;
        }

        String isMockUserLogin = arguments[2];
        if (BaseUtil.isEmpty(isMockUserLogin)) {
            isCheckAuthority = true;
        } else {
            isCheckAuthority = Boolean.parseBoolean(isMockUserLogin);
        }
        log.info("Check user authority: {}", Boolean.toString(isCheckAuthority));
    }

    /**
     * 支持参数
     * 当返回值为true执行resolveArgument
     * 如果参数类型是 String，并且参数上有 @VerifiedUser 注解，则使用该参数解析器。
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> type = parameter.getParameterType();
        return type.isAssignableFrom(User.class) && parameter.hasParameterAnnotation(VerifiedUser.class);
    }

    /**
     *该方法的返回值就是参数的具体值
     * @param parameter
     * @param container
     * @param request
     * @param factory
     * @return
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer container,
                                  NativeWebRequest request,
                                  WebDataBinderFactory factory) {

        if (isCheckAuthority) {
            String isAppS = SpringUtil.getProperty("application.isapp");
            boolean isApp = isAppS.equals("1") ? true : false;
            HttpServletRequest sRequest = (HttpServletRequest)request.getNativeRequest();
            if(isApp){
                String signKey = SpringUtil.getProperty("application.sign.key");
                String sign = sRequest.getParameter(signKey);
                if(!BaseUtil.isEmpty(sign)){
                    BigInteger userId = SignUtil.parseSign(sign);
                    log.info("userId: {}, sign: {}", userId, sign);
                    if (!BaseUtil.isEmpty(userId)) {
                        return userService.getById(userId);
                    }
                }
                return null;
            }else{
                HttpSession session = sRequest.getSession(false);
                if(BaseUtil.isEmpty(session)){
                    return null;
                }
                String signKey = SpringUtil.getProperty("application.session.key");
                Object value = session.getAttribute(signKey);
                if (value == null) {
                    return null;
                }

                String sValue = (String)value;
                return JSON.parseObject(sValue, User.class);
            }

        }

        return userService.getById(BigInteger.valueOf(1));
    }
}
