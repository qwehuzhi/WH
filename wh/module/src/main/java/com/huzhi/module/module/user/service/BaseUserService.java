package com.huzhi.module.module.user.service;

import com.huzhi.module.module.user.entity.User;
import com.huzhi.module.module.user.mapper.UserMapper;
import com.huzhi.module.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

import static com.huzhi.module.module.user.service.UserDefine.GENDER_MALE;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author huzhi
 * @since 2023-02-13
 */
@Service
@Slf4j
public class BaseUserService {
    @Resource
    private UserMapper mapper;


    public BigInteger insert(User user) {
        mapper.insert(user);
        return user.getId();
    }

    public User getById(BigInteger id) {
        return mapper.getById(id);
    }
    public User extractById(BigInteger id){
        return mapper.extractById(id);
    }

    public User getByPhone(String phone, String countryCode) {
        return mapper.getByPhone(phone, countryCode);
    }

    public User getByPhone(String phone) {
        return mapper.getByPhone(phone, "86");
    }

    public User extractByPhone(String phone, String countryCode) {
        return mapper.extractByPhone(phone, countryCode);
    }

    public User extractByEmail(String email){
        return mapper.extractByEmail(email);
    }

    public User extractUserByWxOpenId(String wechatOpenId){
        return mapper.extractUserByWxOpenId(wechatOpenId);
    }

    public int update(User user) {
        return mapper.update(user);
    }

    /**
     * 不安全的插入操作
     * @param user
     */
    public void unsafeUpdate(User user) {
        int result = mapper.update(user);
        if(result == 0){
            throw new RuntimeException("update error");
        }
    }

    public int delete(BigInteger userId) {
        return mapper.delete(userId, BaseUtil.currentSeconds());
    }

    /**
     * 刷新用户登录上下文（最后登录时间和登录ip）
     * @param id
     * @param ip
     * @param time
     */
    public void refreshUserLoginContext(BigInteger id, String ip, int time) {
        User user = new User();
        user.setId(id);
        user.setLastLoginIp(ip);
        user.setLastLoginTime(time);
        user.setUpdateTime(time);
        try {
            update(user);
        } catch (Exception exception) {

        }

    }

    /**
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BigInteger registerUser(String name,String phone,Integer gender,String avatar,String password,
                                   String country,String province, String city,String ipAddress){
        if (BaseUtil.isEmpty(avatar)) {
            avatar = gender.equals(GENDER_MALE.getCode()) ? ImageUtil.getDefaultMaleAvatar() : ImageUtil.getDefaultFeMaleAvatar();
        }
        User newUser = new User();
        int now = BaseUtil.currentSeconds();
        newUser.setUsername(name);
        newUser.setPhone(phone);
        newUser.setCountryCode("86");
        newUser.setGender(gender);
        newUser.setAvatar(avatar);
        newUser.setPassword(SignUtil.marshal(password));
        newUser.setCountry(country);
        newUser.setProvince(province);
        newUser.setCity(city);
        newUser.setRegisterTime(now);
        newUser.setLastLoginTime(now);
        newUser.setRegisterIp(ipAddress);
        newUser.setLastLoginIp(ipAddress);
        newUser.setCreateTime(now);
        newUser.setIsDeleted(0);
        insert(newUser);

        return newUser.getId();
    }

    public boolean login(String phone, String countryCode, String password,
                         boolean noPasswd, boolean remember, int lifeTime) {
        if (lifeTime < 0) {
            return false;
        }
        //check phone
        if (!DataUtil.isPhoneNumber(phone) || BaseUtil.isEmpty(countryCode)) {
            return false;
        }
        User user = getByPhone(phone, countryCode);
        if (BaseUtil.isEmpty(user)) {
            return false;
        }

        if (noPasswd ||
                SignUtil.marshal(password).equals(user.getPassword())) {
            //write session
            //lifeTime = remember ? lifeTime : 0;
            return true;
        } else {
            return false;
        }
    }

    public boolean login(String phone, String password) {
        return login(phone, "86",password, false, true, SignUtil.getExpirationTime());
    }

    public boolean login(String phone,  String password,boolean noPasswd, boolean remember) {
        return login(phone, "86",password, noPasswd, remember, SignUtil.getExpirationTime());
    }

//    public boolean bindPhoneForEmailUser(User user,String countryCode,String phone){
//        if(DataUtils.isPhoneNumber(phone)){
//            return false;
//        }
//        User upUser = new User();
//        upUser.setId(user.getId());
//        upUser.setPhone(phone);
//        upUser.setCountryCode(countryCode);
//        try {
//            update(user);
//            return true;
//        } catch (Exception exception) {
//            return false;
//        }
//    }

    /**
     * 修改密码
     * @param user
     * @param password
     * @return
     */
    public boolean changePassword(User user,String password){
        User upUser = new User();
        upUser.setId(user.getId());
        upUser.setPassword(SignUtil.marshal(password));
        try {
            update(upUser);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 更换头像
     * @param user
     * @param avatar
     * @return
     */
    public boolean changeAvatar(User user,String avatar){
        User upUser = new User();
        upUser.setId(user.getId());
        upUser.setAvatar(avatar);
        try {
            update(upUser);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 更改名字
     * @param user
     * @param username
     * @return
     */
    public boolean changeUsername(User user,String username){
        User upUser = new User();
        upUser.setId(user.getId());
        upUser.setUsername(username);
        try {
            update(upUser);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 更改封面图像
     * @param user
     * @param image
     * @return
     */
    public boolean changeCoverImage(User user,String image){
        User upUser = new User();
        upUser.setId(user.getId());
        upUser.setCoverImage(image);
        try {
            update(upUser);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 更改个人简介
     * @param user
     * @param personalProfile
     * @return
     */
    public boolean changePersonalProfile(User user,String personalProfile){
        User upUser = new User();
        upUser.setId(user.getId());
        upUser.setPersonalProfile(personalProfile);
        try {
            update(upUser);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     *  更改性别
     * @param user
     * @param gender
     * @return
     */
    public boolean changeGender(User user,Integer gender){
        if(!UserDefine.isGender(gender)){
            throw new RuntimeException("not right gender");
        }
        User upUser = new User();
        upUser.setId(user.getId());
        upUser.setGender(gender);
        try {
            update(upUser);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 通过名字查询
     * @param username
     * @return
     */
    public User getByUsername(String username){
        return mapper.getByUsername(username);
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param username
     * @param phone
     * @return
     */
    public List<User> getUsersForConsole(int page, int pageSize, String username, String phone) {
        int begin = (page - 1) * pageSize;
        return mapper.getUsersForConsole(begin, pageSize, "desc", username,phone);
    }

    /**
     * 分页查询总数
     * @param username
     * @param phone
     * @return
     */
    public int getUsersTotalForConsole(String username, String phone) {
        return mapper.getUsersTotalForConsole(username,phone);
    }

    /**
     * 获取用于搜索的用户ID
     * @param username
     * @return
     */
    public String getUserIdsForSearch(String username) {
        if (BaseUtil.isEmpty(username)) {
            return "-1";
        }

        List<User> users = mapper.getUsersByUsername(username);
        String userIds;
        if (BaseUtil.isEmpty(users)) {
            userIds = "-1";
        } else {
            StringBuffer bUserIds = new StringBuffer();
            for (User user : users) {
                bUserIds.append(user.getId() + ",");
            }
            bUserIds.deleteCharAt(bUserIds.length() - 1);
            userIds = bUserIds.toString();
        }

        return userIds;
    }
}
