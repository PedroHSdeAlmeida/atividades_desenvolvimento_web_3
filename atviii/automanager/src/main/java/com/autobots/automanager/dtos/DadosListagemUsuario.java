package com.autobots.automanager.dtos;

import java.util.Set;

import org.springframework.hateoas.Links;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.enumeracoes.PerfilUsuario;

public record DadosListagemUsuario(
		Long id,
		String nome,
		String nomeSocial,
		Set<PerfilUsuario> perfil,
		Set<Telefone> telefones,
		Endereco endereco,
		Set<Documento> documentos,
		Set<Email> emails,
		Links links) {

}