package com.duhc.mybatis;

import com.duhc.mybatis.dao.UserDao;
import com.duhc.mybatis.entity.UserInfo;
import com.duhc.mybatis.service.UserService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by duhc on 2017/7/13.x
 */
public class Demo {

    public  static void getConnection() throws IOException, SQLException {
        SqlSessionFactory factory = UserService.getSessionFactory();
        SqlSession sqlSession  = factory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setPagesize(1);
        userInfo1.setPagebegin(1);
        userDao.selectPage(userInfo1);
        System.out.println(userInfo1.getAge());
    }

    public static void main(String[] args) throws IOException, SQLException {
        getConnection();
    }
}
