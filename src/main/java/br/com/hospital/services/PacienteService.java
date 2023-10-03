package br.com.hospital.services;


import br.com.hospital.models.Paciente;
import br.com.hospital.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public Paciente cadastrarPaciente(Paciente paciente) {
        if (paciente.getNome() == null || paciente.getEmail() == null || paciente.getTelefone() == null || paciente.getCpf() == null || paciente.getEndereco() == null) {
            throw new IllegalArgumentException("Todos os campos obrigatórios devem ser preenchidos (nome, email , telefone , cpf, endereco).");
        }
        
        if (pacienteRepository.existsByEmail(paciente.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        if (pacienteRepository.existsByCpf(paciente.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        
        paciente.setAtivo(true);

        return pacienteRepository.save(paciente);
    }

    public Page<Paciente> listarPacientes(Pageable pageable) {
        return pacienteRepository.findAll(pageable);
    }

    public Paciente atualizarPaciente(Long id, Paciente novoPaciente) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);
        if (!pacienteOptional.isPresent()) {
            throw new IllegalArgumentException("Paciente não encontrado.");
        }

        Paciente pacienteExistente = pacienteOptional.get();

        if (novoPaciente.getEmail() != null || novoPaciente.getCpf() != null) {
            throw new IllegalArgumentException("Não é permitido alterar e-mail e/ou CPF.");
        }

        if (novoPaciente.getNome() != null) {
            pacienteExistente.setNome(novoPaciente.getNome());
        }
        if (novoPaciente.getTelefone() != null) {
            pacienteExistente.setTelefone(novoPaciente.getTelefone());
        }
        if (novoPaciente.getEndereco() != null) {
            pacienteExistente.setEndereco(novoPaciente.getEndereco());
        }

        return pacienteRepository.save(pacienteExistente);
    }

    public void excluirPaciente(Long id) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);
        if (!pacienteOptional.isPresent()) {
            throw new IllegalArgumentException("Paciente não encontrado.");
        }

        Paciente paciente = pacienteOptional.get();
        paciente.setAtivo(false);

        pacienteRepository.save(paciente);
    }
}