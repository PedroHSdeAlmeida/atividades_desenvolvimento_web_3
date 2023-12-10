package com.autobots.automanager.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@JsonTypeName("CredencialUsuarioSenha")
public class CredencialUsuarioSenha extends Credencial {
    @Column(nullable = false, unique = true)
    private String nomeUsuario;

    @Column(nullable = false)
    private String senha;

    @JsonProperty("type")
    private final String type = "CredencialUsuarioSenha";

    public CredencialUsuarioSenha() {
        super(); 
        this.criacao = new Date();
    }
}



