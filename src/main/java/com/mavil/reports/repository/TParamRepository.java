package com.mavil.reports.repository;

import com.mavil.reports.model.TparamsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TParamRepository extends JpaRepository<TparamsEntity, Integer> {
}
