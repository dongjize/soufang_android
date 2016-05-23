package com.dong.soufang.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Description: 城市
 * <p>
 * Author: dong
 * Date: 16/3/14
 */
public class City extends BaseModel implements Parcelable {
    @SerializedName("city_code")
    private String cityCode;

    private String name;

    private String province;
    @SerializedName("is_open")
    private boolean isOpen;

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            City city = new City();
            city.cityCode = in.readString();
            city.name = in.readString();
            city.province = in.readString();
            city.isOpen = in.readByte() != 0;
            return city;
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityCode);
        dest.writeString(name);
        dest.writeString(province);
        dest.writeByte((byte) (isOpen ? 1 : 0));
    }
}
