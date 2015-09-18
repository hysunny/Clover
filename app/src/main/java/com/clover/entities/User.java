package com.clover.entities;

import cn.bmob.im.bean.BmobChatUser;

public class User extends BmobChatUser {

    private static final long serialVersionUID = 1L;

    private Integer age;

    private Boolean sex;    //男-false-女-true

    private Integer sleep=2;

    private Integer disease=2;

    private Integer menses=2;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Integer getSleep() {
        return sleep;
    }

    public void setSleep(Integer sleep) {
        this.sleep = sleep;
    }

    public Integer getDisease() {
        return disease;
    }

    public void setDisease(Integer disease) {
        this.disease = disease;
    }

    public Integer getMenses() {
        return menses;
    }

    public void setMenses(Integer menses) {
        this.menses = menses;
    }
}

