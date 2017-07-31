package com.duhc.mybatis.entity;

/**
 * Created by duhc on 2017/7/13.
 */
public class UserInfo extends Pager{
    private long userId;
private String userName;
private int age;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", age=" + age +
                '}';
    }
}
