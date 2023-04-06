package com.uniTech.repos;

import com.uniTech.entities.Currency;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CurrencyRepository extends CrudRepository<Currency,Long>, JpaSpecificationExecutor<Currency> {
    Optional<Currency> findByName(String name);
}
