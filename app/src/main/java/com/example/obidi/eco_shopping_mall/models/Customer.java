package com.example.obidi.eco_shopping_mall.models;

public class Customer {
    private String username;
    private String email;
    private String name;
    private String password;
    private String street;
    private String city;
    private String state;
    private String phone;
    private String photo;

    // no args constructor
    public Customer() {}

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public Customer setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Customer setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Customer setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public Customer setName(String name) {
        this.name = name;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public Customer setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Customer setCity(String city) {
        this.city = city;
        return this;
    }

    public String getState() {
        return state;
    }

    public Customer setState(String state) {
        this.state = state;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public Customer setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Customer setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}
