package com.dong.soufang.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Description: 楼盘/小区
 * <p>
 * Author: dong
 * Date: 16/3/20
 */
public class Estate extends BaseModel {
    private String name;

    private double longitude;

    private double latitude;

    private String address;

    private City city;

    private String district;

    private String type; //类型 1住宅 2别墅 3经济适用房 4商铺

    private String avatar;
    @SerializedName("avg_price")
    private int avgPrice; //均价

    private ArrayList<String> pictures;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(int avgPrice) {
        this.avgPrice = avgPrice;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }


}
