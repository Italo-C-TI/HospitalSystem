package br.com.hospital.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import br.com.hospital.models.Medico;
import br.com.hospital.repositories.MedicoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class MedicoService {
    @Autowired
    private MedicoRepository medicoRepository;

    public void cadastrarMedico(Medico medico) {
        if (medico.getNome() == null || medico.getEmail() == null || medico.getCrm() == null || medico.getEspecialidade() == null) {
            throw new IllegalArgumentException("Todos os campos obrigatórios devem ser preenchidos.");
        }

        if (medicoRepository.existsByEmail(medico.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        if (medicoRepository.existsByCrm(medico.getCrm())) {
            throw new IllegalArgumentException("CRM já cadastrado.");
        }

        medico.setAtivo(true);
        medicoRepository.save(medico);
    }

    public Page<Medico> listarMedicos(Pageable pageable) {
        return medicoRepository.findAll(pageable);
    }

    public void atualizarMedico(Long id, Medico medicoAtualizado) {
        Medico medicoExistente = medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado."));

        if (!Objects.equals(medicoAtualizado.getEmail(), medicoExistente.getEmail())) {
            throw new IllegalArgumentException("Não é permitido alterar o e-mail.");
        }

        if (!Objects.equals(medicoAtualizado.getCrm(), medicoExistente.getCrm())) {
            throw new IllegalArgumentException("Não é permitido alterar o CRM.");
        }

        if (!Objects.equals(medicoAtualizado.getEspecialidade(), medicoExistente.getEspecialidade())) {
            throw new IllegalArgumentException("Não é permitido alterar a especialidade.");
        }

        medicoExistente.setNome(medicoAtualizado.getNome());
        medicoExistente.setTelefone(medicoAtualizado.getTelefone());
        medicoExistente.setEndereco(medicoAtualizado.getEndereco());


        medicoRepository.save(medicoExistente);
    }

    public void inativarMedico(Long id) {
        Medico medicoExistente = medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado."));

        medicoExistente.setAtivo(false);
        medicoRepository.save(medicoExistente);
    }
}