package com.uniTech.repos;

import com.uniTech.entities.Account;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account,Long> , JpaSpecificationExecutor<Account> {
    List<Account> findAllByUser_IdAndActiveTrue(long idUser);
    List<Account> findAllByUser_IdAndSelectedIsTrue(long idUser);
    Optional<Account> findByAccountNumberAndActiveIsTrue(String accountNumber);
}
