package com.duhc.mybatis;

import com.duhc.mybatis.dao.UserDao;
import com.duhc.mybatis.entity.UserInfo;
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
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession  = factory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        UserInfo userInfo = userDao.getUserInfo(1l);
        System.out.println(userInfo.getAge());
    }

    public static void main(String[] args) throws IOException, SQLException {
        getConnection();
    }
}
