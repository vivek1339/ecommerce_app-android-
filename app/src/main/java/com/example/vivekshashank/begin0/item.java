package com.example.vivekshashank.begin0;

import android.net.Uri;

/**
 * Created by vivek on 1/17/2018.
 */

public class item {




    String desc,creator,name,type,url,uid;
    int price;


    public item(){}
    public item( String name,String type, String creator, int price, String desc,String url,String uid) {
        this.type=type;
        this.desc = desc;
        this.creator = creator;
        this.price = price;
        this.name = name;
        this.url=url;
        this.uid=uid;
    }

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }

    public String geturl() {
        return url;
    }

    public void seturl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
