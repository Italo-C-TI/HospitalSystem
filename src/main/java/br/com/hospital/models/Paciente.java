package br.com.hospital.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pacientes")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    
    @Embedded
    private Endereco endereço;

	public Paciente(Long id, String nome, String email, String telefone, String cpf, Endereco endereço) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		this.cpf = cpf;
		this.endereço = endereço;
	}
    
}