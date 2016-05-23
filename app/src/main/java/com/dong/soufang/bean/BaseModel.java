package com.dong.soufang.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Description:
 * <p/>
 * Author: dong
 * Date: 16/3/14
 */
public class BaseModel {
    protected int id;
    @SerializedName("created_at")
    protected String createdAt;
    @SerializedName("updated_at")
    protected String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
