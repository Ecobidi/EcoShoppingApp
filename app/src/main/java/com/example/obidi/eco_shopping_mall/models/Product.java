package com.example.obidi.eco_shopping_mall.models;

public class Product {
    private String id;
    private String name;
    private String description;
    private double sellingPrice;
    private double regularPrice;
    private String imageUrl;

    public Product(String name, double sellingPrice, String imageUrl) {
        this.name = name;
        this.sellingPrice = sellingPrice;
        this.imageUrl = imageUrl;
    }

    public Product(String name, double sellingPrice, double regularPrice) {
        this.name = name;
        this.sellingPrice = sellingPrice;
        this.regularPrice = regularPrice;
    }

    public Product(String name, double sellingPrice, double regularPrice, String imageUrl) {
        this(name, sellingPrice, regularPrice);
        this.setImageUrl(imageUrl);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setRegularPrice(double regularPrice) {
        this.regularPrice = regularPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
