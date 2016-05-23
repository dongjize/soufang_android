package com.dong.soufang.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Description: 轮播图
 * <p>
 * Author: dong
 * Date: 16/3/20
 */
public class Banner {
    @SerializedName("pic_url")
    private String picUrl;

    private String action;

    private String value;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
