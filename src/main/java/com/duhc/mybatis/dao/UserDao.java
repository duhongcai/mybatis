package com.duhc.mybatis.dao;

import com.duhc.mybatis.entity.UserInfo;

import java.util.List;

/**
 * Created by duhc on 2017/7/13.
 */
public interface UserDao {
    UserInfo getUserInfo(Long userId);
    List<UserInfo> selectPage(UserInfo userInfo);
}
