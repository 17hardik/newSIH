package com.example.sih.Atmanirbhar;

import com.example.sih.Registration.Login;

public class AddedProductCardView {
    private String ProductName;
    private String Description;
    private String Location;
    private String Phone;
    private String Price;

    public AddedProductCardView() {
    }

    public AddedProductCardView(String productName, String description, String location, String phone) {
        ProductName = productName;
        Description = description;
        Location = location;
        Phone = phone;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

}
