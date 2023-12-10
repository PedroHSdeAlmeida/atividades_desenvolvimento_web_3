package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@JsonIgnoreProperties
public class Telefone {
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String ddd;
	@Column
	private String numero;
	
	@ManyToOne
    @JoinColumn(name = "usuario_id") 
    private Usuario usuario;
	
	@ManyToOne
    @JoinColumn(name = "cliente_id")
	@JsonBackReference
    private Cliente cliente;

	public void add(Link linkProprio) {
		
	}
}