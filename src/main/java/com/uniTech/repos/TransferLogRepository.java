package com.uniTech.repos;

import com.uniTech.entities.Transactions;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface TransferLogRepository extends CrudRepository<Transactions,Long> , JpaSpecificationExecutor<Transactions> {
}
