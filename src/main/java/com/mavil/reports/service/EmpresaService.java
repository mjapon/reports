package com.mavil.reports.service;

import com.mavil.reports.model.TempresaEntity;
import com.mavil.reports.repository.TEmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private TEmpresaRepository empresaRepository;

    public String getEsquema(Integer empCodigo) {
        Optional<TempresaEntity> optEmpresa =
                empresaRepository.findById(empCodigo);

        if (optEmpresa.isPresent()) {
            return optEmpresa.get().getEmpEsquema();
        } else {
            return "";
        }
    }
}
