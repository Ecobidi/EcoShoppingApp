package com.example.obidi.eco_shopping_mall.models;

import java.util.List;

public class Category {
    private String mName;
    private String mImageUrl;
    private List<Product> mProducts;

    public String getName() {
        return mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public List<Product> getProducts() {
        return mProducts;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public void setProducts(List<Product> products) {
        mProducts = products;
    }
}
