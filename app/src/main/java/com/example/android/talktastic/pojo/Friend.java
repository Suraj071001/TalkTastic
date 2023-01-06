package com.example.android.talktastic.pojo;

public class Friend {
    String name;
    String last_message;
    String friend_id;

    public Friend(String name,String message){
        this.name = name;
        this.last_message = message;
    }

    public Friend() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }
}
