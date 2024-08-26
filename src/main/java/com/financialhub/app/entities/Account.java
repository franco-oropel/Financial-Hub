package com.financialhub.app.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "account_holder_name")
    private String accountHolderName;
    private Double balance;
    @Column(name = "opening_date")
    private Date openingDate;

    // Getters and Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getAccountHolderName() { return accountHolderName; }

    public void setAccountHolderName(String accountHolderName) { this.accountHolderName = accountHolderName; }

    public double getBalance() { return balance; }

    public void setBalance(double balance) { this.balance = balance; }

    public Date getOpeningDate() { return openingDate; }

    public void setOpeningDate(Date openingDate) { this.openingDate = openingDate; }
}
