package br.com.hospital.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.hospital.models.Consulta;
import br.com.hospital.models.Paciente;
import br.com.hospital.repositories.ConsultaRepository;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    @Autowired
    private ConsultaRepository consultaRepository;
    
    @GetMapping("/{id}")
    public List<Consulta> getConsultaByPaciente(@PathVariable("paciente") Paciente paciente) {
		return consultaRepository.findByPaciente(paciente);
    }

}