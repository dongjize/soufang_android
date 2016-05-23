package com.dong.soufang.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Description: 户型
 * <p>
 * Author: dong
 * Date: 16/3/14
 */
public class House extends BaseModel {
    protected String name;

    protected Estate estate;

    protected int area;
    @SerializedName("house_type")
    protected String houseType; //户型

    protected List<String> photos;

    protected String avatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Estate getEstate() {
        return estate;
    }

    public void setEstate(Estate estate) {
        this.estate = estate;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
