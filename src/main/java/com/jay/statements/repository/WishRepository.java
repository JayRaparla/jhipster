package com.jay.statements.repository;

import com.jay.statements.domain.Wish;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Wish entity.
 */
@SuppressWarnings("unused")
public interface WishRepository extends JpaRepository<Wish,Long> {

    @Query("select wish from Wish wish where wish.user.login = ?#{principal.username}")
    List<Wish> findByUserIsCurrentUser();

}
