package br.com.hospital.models;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "consultas")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Paciente paciente;
    
    @ManyToOne
    private Medico medico;    
    private LocalDateTime dataHora;
    private Desmarcar desmarcar = new Desmarcar(false, "");
    
	public Consulta(Paciente paciente, Medico medico, LocalDateTime dataHora) {
		super();
		this.paciente = paciente;
		this.medico = medico;
		this.dataHora = dataHora;
	}
    
	public Consulta() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public Desmarcar getDesmarcar() {
		return desmarcar;
	}

	public void setDesmarcar(Desmarcar desmarcar) {
		this.desmarcar = desmarcar;
	}
    
}