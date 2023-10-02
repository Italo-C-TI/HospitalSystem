package br.com.hospital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import br.com.hospital.models.Medico;
import br.com.hospital.repositories.MedicoRepository;
import br.com.hospital.services.MedicoService;


@RestController
@RequestMapping("/medicos")
public class MedicoController {
    private final MedicoService medicoService;

    @Autowired
    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }
    
    @GetMapping("/medicos")
    public Page<Medico> listarMedicos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String ordenacao) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(ordenacao));
        return medicoService.listarMedicos(pageable);
    }

}