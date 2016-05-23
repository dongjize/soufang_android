package com.dong.soufang.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Description: 新开楼盘
 * <p>
 * Author: dong
 * Date: 16/3/20
 */
public class NewEstate extends Estate {
    private String developer; //开发商
    @SerializedName("open_date")
    private String openDate; //开盘时间
    @SerializedName("houses")
    private List<NewHouse> newHouses;

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public List<NewHouse> getNewHouses() {
        return newHouses;
    }

    public void setNewHouses(List<NewHouse> newHouses) {
        this.newHouses = newHouses;
    }
}
