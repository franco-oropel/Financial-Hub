package com.financialhub.app.repositories;

import com.financialhub.app.entities.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    // Query con JPQL
    /* @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate AND t.account.id = :accountId") */

    // Query con JPQL convertida a SQL nativo
    @Query(value = "SELECT * FROM transactions t WHERE t.date BETWEEN :startDate AND :endDate AND t.account_id = :accountId", nativeQuery = true)
    List<Transaction> findTransactionsByDateRangeAndAccount(@Param("accountId") Long accountId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
