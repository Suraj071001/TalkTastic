package com.example.android.talktastic.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public class Friend implements Serializable {
    String name;
    String phone_number;
    String friend_id;

    public Friend(String name,String phone_number){
        this.name = name;
        this.phone_number = phone_number;
    }

    public Friend() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

}
