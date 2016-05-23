package com.dong.soufang.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description: 用户
 * <p/>
 * Author: dong
 * Date: 16/3/14
 */
public class User extends BaseModel implements Parcelable {
    private String uuid;
    private String mobile;
    private String name;
    private String avatar;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            User user = new User();
            user.name = in.readString();
            user.mobile = in.readString();
            user.uuid = in.readString();
            user.avatar = in.readString();
            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(mobile);
        dest.writeString(name);
        dest.writeString(avatar);
    }
}
