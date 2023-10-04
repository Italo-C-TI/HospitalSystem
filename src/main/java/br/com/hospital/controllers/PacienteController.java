package br.com.hospital.controllers;

import br.com.hospital.dto.PacienteDTO;
import br.com.hospital.models.Paciente;
import br.com.hospital.services.PacienteService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarPaciente(@RequestBody Paciente paciente) {
        try {
            Paciente novoPaciente = pacienteService.cadastrarPaciente(paciente);
            return ResponseEntity.ok(novoPaciente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public Page<PacienteDTO> listarPacientes(
    		@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String ordenacao) {
        Pageable pageable = PageRequest.of(page , size, Sort.by(ordenacao));
        Page<Paciente> paginaPacientes = pacienteService.listarPacientes(pageable);
        
        List<PacienteDTO> pacientesDTO = paginaPacientes
                .stream()
                .map(paciente -> new PacienteDTO(paciente.getNome(), paciente.getEmail(), paciente.getCpf()))
                .collect(Collectors.toList());
        
        return new PageImpl<>(pacientesDTO, pageable, paginaPacientes.getTotalElements());
    }

    @PutMapping("/alterar/{cpf}")
    public ResponseEntity<?> atualizarPaciente(@PathVariable String cpf, @RequestBody Paciente novoPaciente) {
        try {
            Paciente pacienteAtualizado = pacienteService.atualizarPaciente(cpf, novoPaciente);
            return ResponseEntity.ok(pacienteAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/excluir/{cpf}")
    public ResponseEntity<?> excluirPaciente(@PathVariable String cpf) {
        try {
            pacienteService.excluirPaciente(cpf);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
