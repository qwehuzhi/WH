package com.huzhi.console.controller.user;

import com.alibaba.fastjson.JSON;
import com.huzhi.console.annotations.VerifiedUser;
import com.huzhi.console.domain.user.UserInfoVo;
import com.huzhi.module.response.Response;
import com.huzhi.module.module.user.entity.User;
import com.huzhi.module.module.user.service.BaseUserService;
import com.huzhi.module.utils.BaseUtil;
import com.huzhi.module.utils.IpUtil;
import com.huzhi.module.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    private final BaseUserService baseUserService;
    @Autowired
    public UserController(BaseUserService baseUserService){
        this.baseUserService=baseUserService;
    }
@RequestMapping("/user/login/web")
public Response loginWeb(@VerifiedUser User loginUser,
                         HttpSession httpSession,
                         @RequestParam(name = "phone") String phone,
                         @RequestParam(name = "password") String password,
                         @RequestParam(name = "remember") boolean remember) {
    if (!BaseUtil.isEmpty(loginUser)) {
        return new Response(4004);
    }

    boolean result;
    if (remember) {
        result = baseUserService.login(phone, password);
    } else {
        result = baseUserService.login(phone, "86", password, false, false, 0);
    }
    if (!result) {
        return new Response(1010);
    }

    User user = baseUserService.getByPhone(phone);
    HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    baseUserService.refreshUserLoginContext(user.getId(), IpUtil.getIpAddress(request), BaseUtil.currentSeconds());

    UserInfoVo userInfo = new UserInfoVo();
    userInfo.setUserGender(user.getGender());
    userInfo.setUserName(user.getUsername());
    userInfo.setUserPhone(user.getPhone());
    userInfo.setUserAvatar(user.getAvatar());

    // 写session
    httpSession.setAttribute(SpringUtil.getProperty("application.session.key"), JSON.toJSONString(user));

    return new Response(1001,userInfo);
}
}
