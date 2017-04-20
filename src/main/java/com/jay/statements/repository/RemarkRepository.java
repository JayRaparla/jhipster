package com.jay.statements.repository;

import com.jay.statements.domain.Remark;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Remark entity.
 */
@SuppressWarnings("unused")
public interface RemarkRepository extends JpaRepository<Remark,Long> {

}
