package br.com.hospital.services;

import br.com.hospital.dto.ConsultaDTO;
import br.com.hospital.dto.MedicoDTO;
import br.com.hospital.dto.PacienteDTO;
import br.com.hospital.models.Consulta;
import br.com.hospital.models.Desmarcar;
import br.com.hospital.models.Medico;
import br.com.hospital.models.Paciente;
import br.com.hospital.repositories.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    private final int ANTECEDENCIA_MINUTOS = 30;
    private final int HORA_ABERTURA = 7;
    private final int HORA_FECHAMENTO = 19;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoService medicoService;

    public Consulta agendarConsulta(Paciente paciente, Medico medico, LocalDateTime dataHora) {
        validarHorarioConsulta(dataHora);
        validarAntecedenciaMinima(dataHora);
        validarConsultaDuplicada(paciente, dataHora);
        validarMedicoDisponivel(medico, dataHora);
        validarPacienteAtivo(paciente);

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(dataHora);

        return consultaRepository.save(consulta);
    }

    public Consulta agendarConsulta(Paciente paciente, LocalDateTime dataHora) {
        validarHorarioConsulta(dataHora);
        validarAntecedenciaMinima(dataHora);
        validarConsultaDuplicada(paciente, dataHora);
        validarPacienteAtivo(paciente);

        Medico medico = medicoService.encontrarMedicoDisponivel(dataHora);

        if (medico == null) {
            throw new IllegalArgumentException("Não há médicos disponíveis para o horário selecionado.");
        }

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(dataHora);

        return consultaRepository.save(consulta);
    }

    public void cancelarConsulta(Long consultaId, String motivo) {
        Optional<Consulta> consultaOptional = consultaRepository.findById(consultaId);
 
        if (consultaOptional.isPresent()) {
            Consulta consulta = consultaOptional.get();
            Desmarcar desmarcar = new Desmarcar(true, motivo);
            consulta.setDesmarcar(desmarcar);
        } else {
            throw new IllegalArgumentException("Consulta não encontrada.");
        }
    }

    private void validarHorarioConsulta(LocalDateTime dataHora) {
        int hora = dataHora.getHour();
        int minutos = dataHora.getMinute();

        if (hora < HORA_ABERTURA || hora > HORA_FECHAMENTO || (hora == HORA_FECHAMENTO && minutos != 0)) {
            throw new IllegalArgumentException("Horário fora do horário de funcionamento da clínica.");
        }
    }

    private void validarAntecedenciaMinima(LocalDateTime dataHora) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime antecedenciaMinima = agora.plusMinutes(ANTECEDENCIA_MINUTOS);

        if (dataHora.isBefore(antecedenciaMinima)) {
            throw new IllegalArgumentException("O agendamento deve ser feito com pelo menos 30 minutos de antecedência.");
        }
    }

    private void validarConsultaDuplicada(Paciente paciente, LocalDateTime dataHora) {
        LocalDateTime inicioDia = dataHora.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fimDia = inicioDia.plusHours(24);

        List<Consulta> consultas = consultaRepository.findByPacienteAndDataHoraBetween(paciente, inicioDia, fimDia);

        for (Consulta consulta : consultas) {
            if (consulta.getDataHora().equals(dataHora) && consulta.getDesmarcar().getDesmarcado() == false) {
                throw new IllegalArgumentException("Já existe uma consulta marcada para o mesmo horário.");
            }
        }
    }

    private void validarMedicoDisponivel(Medico medico, LocalDateTime dataHora) {
        List<Consulta> consultas = consultaRepository.findByMedicoAndDataHora(medico, dataHora);

        if (!consultas.isEmpty()) {
            throw new IllegalArgumentException("O médico já possui consulta agendada para o mesmo horário.");
        }
    }

    private void validarPacienteAtivo(Paciente paciente) {
        if (!paciente.isAtivo()) {
            throw new IllegalArgumentException("Não é possível agendar consulta para um paciente inativo.");
        }
    }
    
    public Page<ConsultaDTO> listarConsultasDTOPorCpfPaciente(String cpf, Pageable pageable) {
    	 Page<Consulta> consultas =  consultaRepository.findByPacienteCpf(cpf, pageable);
    	 
         Page<ConsultaDTO> consultasDTO = consultas.map(consulta -> {
         	PacienteDTO pacienteDTO = new PacienteDTO(consulta.getPaciente().getNome(),consulta.getPaciente().getEmail(), consulta.getPaciente().getCpf());
         	MedicoDTO medicoDTO = new MedicoDTO(consulta.getMedico().getNome(),consulta.getMedico().getEmail(), consulta.getMedico().getCrm(),consulta.getMedico().getEspecialidade()); 
            ConsultaDTO consultaDTO = new ConsultaDTO(pacienteDTO,medicoDTO, consulta.getDataHora());
         
             return consultaDTO;
         });
         
         return consultasDTO;
    }
    
    public Page<ConsultaDTO> listarConsultasDTOPorCrmMedico(String crm, Pageable pageable){
        Page<Consulta> consultas = consultaRepository.findByMedicoCrm(crm, pageable);

        Page<ConsultaDTO> consultasDTO = consultas.map(consulta -> {
        	PacienteDTO pacienteDTO = new PacienteDTO(consulta.getPaciente().getNome(),consulta.getPaciente().getEmail(), consulta.getPaciente().getCpf());
        	MedicoDTO medicoDTO = new MedicoDTO(consulta.getMedico().getNome(),consulta.getMedico().getEmail(), consulta.getMedico().getCrm(),consulta.getMedico().getEspecialidade()); 
            ConsultaDTO consultaDTO = new ConsultaDTO(pacienteDTO,medicoDTO, consulta.getDataHora());
        
            return consultaDTO;
        });
        
        return consultasDTO;
    }
}
