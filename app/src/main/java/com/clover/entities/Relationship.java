package com.clover.entities;

import cn.bmob.v3.BmobObject;

/**
 * Created by dan on 2015/6/29.
 */
public class Relationship extends BmobObject{
    private User m_user;
    private User w_user;
    private String date;

    public User getM_user() {
        return m_user;
    }

    public User getW_user() {
        return w_user;
    }

    public void setM_user(User m_user) {
        this.m_user = m_user;
    }

    public void setW_user(User w_user) {
        this.w_user = w_user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
