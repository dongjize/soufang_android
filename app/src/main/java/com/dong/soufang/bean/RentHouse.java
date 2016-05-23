package com.dong.soufang.bean;

import java.util.List;

/**
 * Description: 租房
 * <p>
 * Author: dong
 * Date: 16/3/20
 */
public class RentHouse extends House {
    private String rentPrice; //出租价格

    private int floor; //楼层

    private Estate estate; //属于的小区

    private int rentType; //整租 合租

    private List<String> pictures;

    public String getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(String rentPrice) {
        this.rentPrice = rentPrice;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public Estate getEstate() {
        return estate;
    }

    @Override
    public void setEstate(Estate estate) {
        this.estate = estate;
    }

    public int getRentType() {
        return rentType;
    }

    public void setRentType(int rentType) {
        this.rentType = rentType;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}
