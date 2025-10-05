package com.application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
public class Customer {

    @Id
    private String username; // previously private String name

    @OneToOne(mappedBy = "customer") // inverse
    private Cart cart;

    private String firstName;
    private String lastName;
    private String phoneNumber; // previously private String phone
    private String email;
    private String address;
    private String country;
    private String postalCode;
    private String password;
    private String providerCustomerId;

    public Customer() {
    }
}
