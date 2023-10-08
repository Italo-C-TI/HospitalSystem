package br.com.hospital.controllers;

import br.com.hospital.dto.ConsultaDTO;
import br.com.hospital.models.Consulta;
import br.com.hospital.models.Medico;
import br.com.hospital.models.Paciente;
import br.com.hospital.repositories.MedicoRepository;
import br.com.hospital.repositories.PacienteRepository;
import br.com.hospital.services.ConsultaService;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping("/agendar")
    public ResponseEntity<Consulta> agendarConsulta(
            @RequestParam String cpfPaciente,
            @RequestParam(required = false) String crmMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {
        try {
            Optional<Paciente> optionalPaciente = pacienteRepository.findByCpf(cpfPaciente);

            if (!optionalPaciente.isPresent()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            Paciente paciente = optionalPaciente.get();
            
            if (crmMedico != null) {
                Optional<Medico> optionalMedico = medicoRepository.findByCrm(crmMedico);
                
                if(optionalMedico.isPresent()) {
                	Medico medico = optionalMedico.get();
                    Consulta agendamento = consultaService.agendarConsulta(paciente, medico, dataHora);
                    return new ResponseEntity<>(agendamento, HttpStatus.CREATED);
                }
            }
     
            Consulta agendamento = consultaService.agendarConsulta(paciente, dataHora);
            return new ResponseEntity<>(agendamento, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cancelar/{consultaId}")
    public ResponseEntity<String> cancelarConsulta(@PathVariable Long consultaId, @RequestBody String motivo) {
        try {
            consultaService.cancelarConsulta(consultaId, motivo);
            return new ResponseEntity<>("Consulta cancelada com sucesso.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Erro ao cancelar consulta: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/paciente/{cpf}")
    public ResponseEntity<Page<ConsultaDTO>> listarConsultasPorCpfPaciente(
            @PathVariable String cpf, Pageable pageable) {
        Page<ConsultaDTO> consultas = consultaService.listarConsultasDTOPorCpfPaciente(cpf, pageable);
        
        return new ResponseEntity<>(consultas, HttpStatus.OK);
    }

    @GetMapping("/medico/{crm}")
    public ResponseEntity<Page<ConsultaDTO>> listarConsultasPorCrmMedico(
            @PathVariable String crm, Pageable pageable) {
        Page<ConsultaDTO> consultas = consultaService.listarConsultasDTOPorCrmMedico(crm, pageable);

        return new ResponseEntity<>(consultas, HttpStatus.OK);
    }
}