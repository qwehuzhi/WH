package com.huzhi.app.controller.user;

import com.huzhi.app.annotations.VerifiedUser;
import com.huzhi.app.domain.user.UserInfoVo;
import com.huzhi.app.domain.user.UserLoginInfoVo;
import com.huzhi.module.module.user.entity.User;
import com.huzhi.module.module.user.service.BaseUserService;
import com.huzhi.module.module.user.service.UserDefine;
import com.huzhi.module.response.Response;
import com.huzhi.module.utils.BaseUtil;
import com.huzhi.module.utils.IpUtil;
import com.huzhi.module.utils.SignUtil;
import com.huzhi.module.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;

@RestController
public class UserController {
    private final BaseUserService baseUserService;
    @Autowired
    public UserController(BaseUserService baseUserService){
        this.baseUserService=baseUserService;
    }

    /**
     * 用户登录
     * @return
     */
    @RequestMapping("/user/login/app")
    public Response loginApp(@VerifiedUser User loginUser,
                             @RequestParam(name = "phone") String phone,
                             @RequestParam(name = "password") String password) {
        if (!BaseUtil.isEmpty(loginUser)) {
            return new Response(4004);
        }

        //合法用户直接登录
        boolean result = baseUserService.login(phone, password);
        if (!result) {
            return new Response(4004);
        }
        User user = baseUserService.getByPhone(phone);

        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        baseUserService.refreshUserLoginContext(user.getId(), IpUtil.getIpAddress(request), TimeUtil.getNowTime());
        UserInfoVo userInfo = new UserInfoVo();
        userInfo.setGender(user.getGender());
        userInfo.setName(user.getUsername());
        userInfo.setPhone(user.getPhone());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setUserId(user.getId());

        UserLoginInfoVo loginInfo = new UserLoginInfoVo();
        loginInfo.setSign(SignUtil.makeSign(user.getId()));

        loginInfo.setUserInfo(userInfo);
        return new Response(1001,loginInfo);
    }

    /**
     * 用户注册
     * @return
     */
    @RequestMapping("/user/register/app")
    public Response registerApp(@VerifiedUser User loginUser,
                                @RequestParam(name = "phone") String phone,
                                @RequestParam(name = "gender") Integer gender,
                                @RequestParam(name = "avatar", required = false) String avatar,
                                @RequestParam(name = "name") String name,
                                @RequestParam(name = "password") String password,
                                @RequestParam(name = "country", required = false) String country,
                                @RequestParam(name = "province", required = false) String province,
                                @RequestParam(name = "city", required = false) String city) {
        if (!BaseUtil.isEmpty(loginUser)) {
            return new Response(4004);
        }


        //考虑用户已经注册了
        //即phone存在
        //直接按照登录处理，返回sign
        User user = baseUserService.extractByPhone(phone,"86");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        BigInteger newUserId;
        if(!BaseUtil.isEmpty(user)){
            //如果用户被禁止登录
            if(user.getIsDeleted().equals(1) || user.getIsBan().equals(1)){
                return new Response(1010);
            }
            newUserId = user.getId();
            baseUserService.refreshUserLoginContext(user.getId(), IpUtil.getIpAddress(request), TimeUtil.getNowTime());
        }else {
            //注册新用户
            if (!UserDefine.isGender(gender)) {
                return new Response(2014);
            }
            try {
                newUserId = baseUserService.registerUser(name, phone, gender, avatar, password,country, province, city, IpUtil.getIpAddress(request));
            } catch (Exception exception) {
                return new Response(4004);
            }

        }
        user = baseUserService.getById(newUserId);

        UserInfoVo userInfo = new UserInfoVo();
        userInfo.setGender(user.getGender());
        userInfo.setName(user.getUsername());
        userInfo.setPhone(user.getPhone());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setUserId(user.getId());

        UserLoginInfoVo loginInfo = new UserLoginInfoVo();
        loginInfo.setSign(SignUtil.makeSign(user.getId()));
        loginInfo.setUserInfo(userInfo);
        return new Response(1001,loginInfo);
    }
}
