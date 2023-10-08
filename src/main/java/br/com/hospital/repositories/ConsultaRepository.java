package br.com.hospital.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.hospital.models.Consulta;
import br.com.hospital.models.Medico;
import br.com.hospital.models.Paciente;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPacienteAndDataHoraBetween(Paciente paciente, LocalDateTime inicio, LocalDateTime fim);
    List<Consulta> findByMedicoAndDataHora(Medico medico, LocalDateTime dataHora);
    
    @Query("SELECT c FROM Consulta c WHERE c.paciente.cpf = :cpf")
    Page<Consulta> findByPacienteCpf(@Param("cpf") String cpf, Pageable pageable);
    @Query("SELECT c FROM Consulta c WHERE c.medico.crm = :crm")
    Page<Consulta> findByMedicoCrm(@Param("crm") String crm, Pageable pageable);

}