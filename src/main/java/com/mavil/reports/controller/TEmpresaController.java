package com.mavil.reports.controller;

import com.mavil.reports.model.TempresaEntity;
import com.mavil.reports.repository.TEmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/empresa")
public class TEmpresaController {

    @Autowired
    private TEmpresaRepository empresaRepository;

    @GetMapping
    public List<TempresaEntity> getAllEmp() {
        return empresaRepository.findAll();
    }
}
