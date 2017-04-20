package com.jay.statements.repository;

import com.jay.statements.domain.Transaction;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Transaction entity.
 */
@SuppressWarnings("unused")
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    @Query("select transaction from Transaction transaction where transaction.user.login = ?#{principal.username}")
    List<Transaction> findByUserIsCurrentUser();

    @Query("select distinct transaction from Transaction transaction left join fetch transaction.tags left join fetch transaction.remarks")
    List<Transaction> findAllWithEagerRelationships();

    @Query("select transaction from Transaction transaction left join fetch transaction.tags left join fetch transaction.remarks where transaction.id =:id")
    Transaction findOneWithEagerRelationships(@Param("id") Long id);

}
