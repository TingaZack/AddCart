package com.zack.tinga.applicationtest;

/**
 * Created by admin on 2017/07/28.
 */

public class Product {

    String id, name, type, img;
    double price;

    public Product() {
    }

    public Product(String id, String name, String type, String img, double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.img = img;
        this.price = price;
    }

    public Product(String id, String name, String type, double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
