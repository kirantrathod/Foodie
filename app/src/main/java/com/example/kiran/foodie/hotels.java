package com.example.kiran.foodie;

public class hotels {
    public hotels(String name, String category, String address) {
        this.name = name;
        this.category = category;
        this.address = address;
    }

    public String name;
    public String category;
    public String address;

    public hotels() {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
