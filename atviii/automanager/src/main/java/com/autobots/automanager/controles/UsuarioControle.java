package com.autobots.automanager.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.AdicionadorLinkUsuario;
import com.autobots.automanager.modelo.UsuarioAtualizador;
import com.autobots.automanager.modelo.UsuarioCadastro;
import com.autobots.automanager.modelo.UsuarioSelecionador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

import antlr.collections.List;
import lombok.extern.slf4j.Slf4j;

@RestController
public class UsuarioControle {

	@Autowired
	private UsuarioRepositorio repositorio;
	@Autowired
	private UsuarioAtualizador usuarioAtualizacao;
	@Autowired
	private UsuarioSelecionador selecionador;
	@Autowired
	private AdicionadorLinkUsuario adicionadorLink;
	

	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
		List<Usuario> usuarios = repositorio.findAll();
		Usuario usuario = selecionador.selecionar(usuarios, id);
		if (usuario == null) {
			ResponseEntity<Usuario> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(usuario);
			ResponseEntity<Usuario> resposta = new ResponseEntity<Usuario>(usuario, HttpStatus.FOUND);
			return resposta;
		}
	}


	@PostMapping("/usuario/cadastro")
	public ResponseEntity<?> cadastrarCliente(@RequestBody Usuario usuario) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (usuario.getId() == null) {
			repositorio.save(usuario);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);

	}

	@PutMapping("/usuario/{id}")
	public ResponseEntity<EntityModel<Usuario>> atualizarUsuario(@PathVariable Long id,
			@RequestBody Usuario usuarioAtualizado) {
		try {

			Usuario atualizacao= usuarioAtualizacao.atualizarUsuario(id, usuarioAtualizado);
			Link selfLink = Link.of(String.format("/usuarios/%d", atualizacao.getId())).withSelfRel();
			EntityModel<Usuario> resource = EntityModel.of(atualizacao, selfLink);

			return new ResponseEntity<>(resource, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	
	/*
	 * @GetMapping("/usuarios") public ResponseEntity<List<Usuario>> obterUsuarios()
	 * { List<Usuario> usuarios = repositorio.findAll(); if (usuarios.isEmpty()) {
	 * ResponseEntity<List<Usuario>> resposta = new
	 * ResponseEntity<>(HttpStatus.NOT_FOUND); return resposta; } else {
	 * adicionadorLink.adicionarLink(usuarios); ResponseEntity<List<Usuario>>
	 * resposta = new ResponseEntity<>(usuarios, HttpStatus.FOUND); return resposta;
	 * } }
	 */	 
	@DeleteMapping("/usuario/excluir")
	public ResponseEntity<?> excluirCliente(@RequestBody Usuario exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Usuario usuario = repositorio.getById(exclusao.getId());
		if (usuario != null) {
			repositorio.delete(usuario);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}


}
