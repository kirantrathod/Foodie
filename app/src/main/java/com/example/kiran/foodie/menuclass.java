package com.example.kiran.foodie;

public class menuclass {
    public menuclass() {
    }
    String name_of_menu,price;

    public menuclass(String name_of_menu, String price) {
        this.name_of_menu = name_of_menu;
        this.price = price;
    }

    public String getName_of_menu() {
        return name_of_menu;
    }

    public void setName_of_menu(String name_of_menu) {
        this.name_of_menu = name_of_menu;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
