package com.left.im.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class Goods extends BmobObject implements Serializable {
    private String name;                // 商品名
    private String price;               // 商品价格
    private String describe;            // 商品描述
    private String photo;             // 商品主图
    private String[] category;          //商品所属类目

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }
}
