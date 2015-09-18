package com.clover.entities;

import cn.bmob.v3.BmobObject;

/**
 * Created by dan on 2015/6/29.
 */
public class Mood extends BmobObject{
    private String md_content;
    private String md_path;
    private String belongId;

    public String getMd_content() {
        return md_content;
    }

    public void setMd_content(String md_content) {
        this.md_content = md_content;
    }

    public String getMd_path() {
        return md_path;
    }

    public void setMd_path(String md_path) {
        this.md_path = md_path;
    }

    public String getBelongId() {
        return belongId;
    }

    public void setBelongId(String belongId) {
        this.belongId = belongId;
    }
}
