package com.financialhub.app.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "account_holder_name")
    private String accountHolderName;
    private Double balance;
    @Column(name = "opening_date")
    private Date openingDate;
}
