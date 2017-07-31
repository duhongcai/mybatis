package com.duhc.mybatis.service;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;

/**
 * Created by duhc on 2017/7/31.
 */
public class UserService {
    private static SqlSessionFactory sqlSessionFactory;
    private static Reader reader;

    static {
        try{
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static SqlSessionFactory getSessionFactory(){
        return sqlSessionFactory;
    }
}
