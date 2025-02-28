package com.example.oujda_shop.entities;


import java.io.Serializable;

public class Category implements Serializable {
    private Integer id;
    private String name;
    private String imageResource;
    private String createdAt;
    private String description;

    public Category(Integer id, String name,String description, String imageResource, String createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageResource = imageResource;
        this.createdAt = createdAt;
    }

    public Category(Integer id, String name, String imageResource, String createdAt) {
        this.id = id;
        this.name = name;
        this.imageResource = imageResource;
        this.createdAt = createdAt;
    }
    public Category(String name,String description, String imageResource) {
        this.name = name;
        this.imageResource = imageResource;
        this.description =description;
    }
    public Category(String name) {
        this.name = name;

    }

    // Getters
    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getImageResource() { return imageResource; }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
