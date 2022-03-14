package com.vtc.eatlocal.entity;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
@Table(name= "restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String restaurantName;

    private String email;

    @Transient
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}