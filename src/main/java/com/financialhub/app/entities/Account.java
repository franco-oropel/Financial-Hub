package com.financialhub.app.entities;

import com.financialhub.app.validations.account.ValidAccountType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "account_holder_name", nullable = false)
    private String accountHolderName;

    @Column(nullable = false)
    @ValidAccountType // Custom Annotation
    private String type;

    @Column(nullable = false)
    private Double balance;

    @Column(name = "opening_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime openingDate;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (openingDate == null) {
            openingDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);; // Sets the current date if one is not provided
        }
    }
}
