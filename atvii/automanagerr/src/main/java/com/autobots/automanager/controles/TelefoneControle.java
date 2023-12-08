package com.autobots.automanager.controles;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.autobots.automanager.DTOs.TelefoneRecord;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.AdicionadorLinkTelefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.modelo.TelefoneCadastro;
import com.autobots.automanager.modelo.TelefoneDeletor;
import com.autobots.automanager.modelo.TelefoneSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
public class TelefoneControle {
	@Autowired
	private TelefoneRepositorio telefoneRepositorio;
	@Autowired
	private TelefoneSelecionador selecionar;
	@Autowired
    private TelefoneDeletor telefoneDeletor;
	@Autowired
	private TelefoneCadastro telefoneCadastro;
	@Autowired
	private TelefoneAtualizador telefoneAtualizador;
	@Autowired
	private AdicionadorLinkTelefone adicionadorLink;
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	
	@GetMapping("telefone/{id}")
	public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
		List<Telefone> telefones = telefoneRepositorio.findAll();
		Telefone telefone = selecionar.selecionar(telefones, id);
		if (telefone == null) {
			ResponseEntity<Telefone> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(telefone);
			ResponseEntity<Telefone> resposta = new ResponseEntity<Telefone>(telefone, HttpStatus.FOUND);
			return resposta;
		

		}
	}
	
	@GetMapping("/telefones")
	public ResponseEntity<List<Telefone>> obterTelefones() {
		List<Telefone> telefones = telefoneRepositorio.findAll();
		if (telefones.isEmpty()) {
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(telefones);
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(telefones, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@DeleteMapping("/telefone/excluir/{id}")
	public ResponseEntity<Void> excluirTelefone(@PathVariable long id) {
	    HttpStatus status = HttpStatus.OK;
	    
	    java.util.Optional<Telefone> telefone = telefoneRepositorio.findById(id);
	    
	    if (telefone.isPresent()) {
			telefoneDeletor.excluirTelefoneDeClientes(id);
	    } else {
	        status = HttpStatus.NOT_FOUND;
	    }
	    
	    return new ResponseEntity<>(status);
	    }
	
	@PostMapping("/telefone/cadastro/{id}")
    public ResponseEntity<?> cadastrarTelefone(@PathVariable long id, @RequestBody Telefone novoTelefone) {
	    HttpStatus status = HttpStatus.CREATED;

	    Cliente cliente = clienteRepositorio.findById(id).orElse(null);

	    if (cliente != null) {
	    	telefoneCadastro.cadastrarTelefone(id, novoTelefone);

	        Telefone telefone = telefoneRepositorio.findById(novoTelefone.getId()).get();
	        adicionadorLink.adicionarLink(telefone);
	    } else {
	        status = HttpStatus.NOT_FOUND;
	    }

	    return new ResponseEntity<>(status);

    }
	
    @PutMapping("/telefone/atualizar")
    @Transactional
    public ResponseEntity<?> atualizarTelefones(@RequestBody TelefoneRecord atualizacoes) {
        List<Telefone> telefones = telefoneRepositorio.findAll();
        telefoneAtualizador.atualizar(telefones, atualizacoes.telefones());
	    Link selfLink = Link.of("/telefone/atualizar").withRel("self");

	    for (Telefone telefone: telefones) {
	        adicionadorLink.adicionarLink(telefone, selfLink);
	    }

	    return telefones.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().build();

    }

}
