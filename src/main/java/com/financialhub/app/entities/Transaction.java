package com.financialhub.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.financialhub.app.validations.transaction.ValidTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @ValidTransactionType // Custom Annotation
    private String type;

    @Column(nullable = false, updatable = false)
    private Double amount;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @PrePersist
    protected void onCreate() {
        if (date == null) {
            date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);; // Sets the current date if one is not provided
        }
    }
}
