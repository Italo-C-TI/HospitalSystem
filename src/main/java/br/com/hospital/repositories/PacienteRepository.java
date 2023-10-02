package br.com.hospital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.hospital.models.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}