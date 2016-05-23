package com.dong.soufang.bean;

/**
 * Description: 新房
 * <p>
 * Author: dong
 * Date: 16/3/22
 */
public class NewHouse extends House {
    private int storage;
    private int price;
    private String decoration;

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDecoration() {
        return decoration;
    }

    public void setDecoration(String decoration) {
        this.decoration = decoration;
    }
}
