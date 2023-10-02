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
    private Medico médico;    
    private LocalDateTime dataHora;
    
	public Consulta(Long id, Paciente paciente, Medico médico, LocalDateTime dataHora) {
		super();
		this.id = id;
		this.paciente = paciente;
		this.médico = médico;
		this.dataHora = dataHora;
	}
    
    
}