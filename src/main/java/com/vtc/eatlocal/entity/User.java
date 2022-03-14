package com.vtc.eatlocal.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity // This tells Hibernate to make a table out of this class
@Table(name= "user")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer customerId;

    private String fname;

    private String lname;

    private String email;

    @Transient
    private String password;

    @ManyToMany
    @JoinTable(
            name = "customer_challenge",
            joinColumns = @JoinColumn(name = "customerId"),
            inverseJoinColumns = @JoinColumn(name = "challengeId"))
    private Set<LiveChallenge> customerChallenges = new HashSet<>();


    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public Set<LiveChallenge> getCustomerChallenges() {
        return customerChallenges;
    }

    public void setCustomerChallenges(Set<LiveChallenge> customerChallenges) {
        this.customerChallenges = customerChallenges;
    }
}