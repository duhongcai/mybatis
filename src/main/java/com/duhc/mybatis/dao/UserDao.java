package com.duhc.mybatis.dao;

import com.duhc.mybatis.entity.UserInfo;

/**
 * Created by duhc on 2017/7/13.
 */
public interface UserDao {
    UserInfo getUserInfo(Long userId);
}
