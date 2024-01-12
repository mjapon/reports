package com.mavil.reports.repository;

import com.mavil.reports.model.TempresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TEmpresaRepository extends JpaRepository<TempresaEntity, Integer> {
}
