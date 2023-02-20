package com.huzhi.module.module.user.mapper;

import com.huzhi.module.module.user.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author huzhi
 * @since 2023-02-13
 */
@Mapper
public interface UserMapper {
    /**
     * 未禁用且未删除的用户信息
     * @param id
     * @return
     */
    @Select("select * from user WHERE id=#{id} and is_ban = 0 and is_deleted = 0")
    User getById(@Param("id") BigInteger id);

    /**
     * 取出指定id用户信息
     * @param id
     * @return
     */
    @Select("select * from user WHERE id=#{id}")
    User extractById(@Param("id") BigInteger id);

    /**
     * 根据手机号码和国家代码查询未禁用未删除的用户信息
     * @param phone
     * @param countryCode
     * @return
     */
    @Select("select * from user WHERE phone=#{phone} and country_code=#{countryCode} and is_ban = 0 and is_deleted = 0")
    User getByPhone(@Param("phone") String phone, @Param("countryCode") String countryCode);

    /**
     * 根据手机号和国家代码取出指定id的信息
     * @param phone
     * @param countryCode
     * @return
     */
    @Select("select * from user WHERE phone=#{phone} and country_code=#{countryCode}")
    User extractByPhone(@Param("phone") String phone, @Param("countryCode") String countryCode);

    /**
     * 根据邮箱查询用户信息
     * @param email
     * @return
     */
    @Select("select * from user WHERE email=#{email}")
    User extractByEmail(@Param("email") String email);

    /**
     * 根据用户昵称查询未禁用未删除的用户信息
     * @param username
     * @return
     */
    @Select("select * from user WHERE username=#{username} and is_ban = 0 and is_deleted = 0")
    User getByUsername(@Param("username") String username);

    /**
     * 根据微信id取出用户信息
     * @param wechatOpenId
     * @return
     */
    @Select("select * from user WHERE wechat_open_id=#{wechatOpenId}")
    User extractUserByWxOpenId(@Param("wechatOpenId") String wechatOpenId);

    /**
     * 修改
     * @param user
     * @return
     */
    int update(@Param("user") User user);

    /**
     * 新增
     * @param user
     * @return
     */
    int insert(@Param("user") User user);

    /**
     * 根据id逻辑删除
     * @param id
     * @param time
     * @return
     */
    @Update("update user set is_deleted=1, update_time=#{time} where id=#{id}")
    int delete(@Param("id") BigInteger id, @Param("time") Integer time);

    /**
     * 根据用户名称查询有效用户数据
     * @param username
     * @return
     */
    @Select("select * from user WHERE username like concat('%',#{id},'%') and is_ban = 0 and is_deleted = 0")
    List<User> getUsersByUsername(@Param("username") String username);

    /**
     * 根据电话号码和用户名称模糊查询并分页
     * @param begin     分页开始下标
     * @param size      页大小
     * @param orderBy   选择正序还是倒序
     * @param username
     * @param phone
     * @return
     */
    List<User> getUsersForConsole(@Param("begin") int begin, @Param("size") int size, @Param("orderBy") String orderBy,
                                  @Param("username") String username, @Param("phone") String phone);

    /**
     * 根据电话号码和用户名称模糊查询并分页时计算总数
     * @param username
     * @param phone
     * @return
     */
    int getUsersTotalForConsole(@Param("username") String username, @Param("phone") String phone);
}
