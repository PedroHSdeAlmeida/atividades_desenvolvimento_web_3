package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.hateoas.Link;

import lombok.Data;

@Data
@Entity
public class Documento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String tipo;

	@Column(unique = true)
	private String numero;
	
	@ManyToOne
    @JoinColumn(name = "usuario_id") 
    private Usuario usuario;

	@ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;


	public void add(Link linkProprio) {
		
	}
}
