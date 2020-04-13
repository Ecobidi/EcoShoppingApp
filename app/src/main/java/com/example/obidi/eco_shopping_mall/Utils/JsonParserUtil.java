package com.example.obidi.eco_shopping_mall.Utils;

import com.example.obidi.eco_shopping_mall.models.CartItem;
import com.example.obidi.eco_shopping_mall.models.Category;
import com.example.obidi.eco_shopping_mall.models.Customer;
import com.example.obidi.eco_shopping_mall.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParserUtil {

    public static List<CartItem> mapJsonStringToCart(String json) throws JSONException {
        List<CartItem> cart = new ArrayList<>();
        JSONArray root = new JSONArray(json);

        for (int i = 0, len = root.length(); i < len; i++) {
            JSONObject obj = root.getJSONObject(i);
            Product product = mapJsonStringToProduct(obj.getJSONObject("product").toString());
            int quantity = obj.getInt("quantity");
            CartItem item = new CartItem(product, quantity);
            cart.add(item);
        }
        return cart;
    }

    public static List<Product> mapJsonStringToProducts(String jsonString) throws JSONException {
        List<Product> products = new ArrayList<>();
        JSONObject root = new JSONObject(jsonString);
        JSONArray arr = root.optJSONArray("products");
        for (int i = 0; i < arr.length(); i++) {
            products.add(mapJsonStringToProduct(arr.getJSONObject(i).toString()));
        }
        return products;
    }

    public static Product mapJsonStringToProduct(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        Product product = new Product(obj.optString("name"), obj.optDouble("sellingPrice"),
                obj.optDouble("regularPrice"));
        product.setId(obj.optString("_id"));
        product.setDescription((obj.optString("description")));
        JSONArray images = obj.optJSONArray("image");
        if (images.length() > 0) product.setImageUrl(images.getString(0));
        return product;
    }

    public static List<Category> mapJsonStringToCategories(String jsonString, boolean withProducts) throws JSONException {
        List<Category> categories = new ArrayList<>();
        JSONArray root = new JSONArray(jsonString);
        for (int i = 0; i < root.length(); i++) {
            JSONObject obj = root.getJSONObject(i);
            Category category = new Category();
            category.setName(obj.getString("name"));
            category.setImageUrl(obj.getString("image"));
            if (withProducts) { // retrieve the products under the category
                List<Product> products = JsonParserUtil
                        .mapJsonStringToProducts(obj.toString());
                category.setProducts(products);
            }
            categories.add(category);
        }
        return categories;
    }

    public static Customer mapJsonStringToCustomer(String jsonString) throws JSONException {
        Customer customer = new Customer();
        JSONObject root = new JSONObject(jsonString);
        customer.setUsername(root.getString("username"))
                .setName(root.getString("name"))
                .setEmail(root.getString("email"))
                .setPassword(root.getString("password"))
                .setPhone(root.optString("phone"))
                .setPhoto(root.optString("photo"));
        JSONObject addressObj = root.getJSONObject("address");
        customer.setCity(addressObj.optString("city"))
                .setState(addressObj.optString("state"))
                .setStreet(addressObj.optString("street"));
        return customer;
    }
}
