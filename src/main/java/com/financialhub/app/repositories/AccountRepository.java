package com.financialhub.app.repositories;

import com.financialhub.app.entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
    boolean existsByTypeAndAccountHolderName(String type, String accountHolderName);
}