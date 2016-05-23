package com.dong.soufang.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Description: 区县
 * <p/>
 * Author: dong
 * Date: 16/3/14
 */
public class District extends BaseModel {
    private String name;
    @SerializedName("city_id")
    private int cityId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
