package com.financialhub.app.repositories;

import com.financialhub.app.entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long> {
    boolean existsByTypeAndAccountHolderName(String type, String accountHolderName);
    List<Account> findByAccountHolderName(String accountHolderName);
}