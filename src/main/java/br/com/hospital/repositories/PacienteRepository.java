package br.com.hospital.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.hospital.models.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
	   boolean existsByEmail(String email);
	   boolean existsByCpf(String cpf);
	   Optional<Paciente> findByCpf(String cpf);
}