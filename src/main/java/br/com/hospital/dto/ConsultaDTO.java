package br.com.hospital.dto;

import java.time.LocalDateTime;

public class ConsultaDTO {
    private PacienteDTO paciente;    
    private MedicoDTO medico;    
    private LocalDateTime dataHora;
    
    public ConsultaDTO(PacienteDTO paciente, MedicoDTO medico, LocalDateTime dataHora) {
		super();
		this.paciente = paciente;
		this.medico = medico;
		this.dataHora = dataHora;
	}
	
	public PacienteDTO getPaciente() {
		return paciente;
	}
	public void setPaciente(PacienteDTO paciente) {
		this.paciente = paciente;
	}
	public MedicoDTO getMedico() {
		return medico;
	}
	public void setMedico(MedicoDTO medico) {
		this.medico = medico;
	}
	public LocalDateTime getDataHora() {
		return dataHora;
	}
	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}
    
    
}
