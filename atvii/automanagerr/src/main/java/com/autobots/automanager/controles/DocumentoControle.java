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
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.DTOs.DocumentoRecord;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.AdicionadorLinkDocumento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.DocumentoCadastro;
import com.autobots.automanager.modelo.DocumentoDeletor;
import com.autobots.automanager.modelo.DocumentoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
public class DocumentoControle {
	@Autowired
	private DocumentoRepositorio documentoRepositorio;
	@Autowired
	private DocumentoSelecionador selecionador;
	@Autowired
    private DocumentoDeletor documentoDeletor;
	@Autowired
    private DocumentoCadastro documentoCadastro;
	@Autowired
	private DocumentoAtualizador documentoAtualizador;
	@Autowired
	private AdicionadorLinkDocumento adicionadorLink;
	@Autowired
	private ClienteRepositorio clienteRepositorio;


	
	@GetMapping("documento/{id}")
	public ResponseEntity<Documento> obterDocumento(@PathVariable long id) {
		List<Documento> documentos = documentoRepositorio.findAll();
		Documento documento = selecionador.selecionar(documentos, id);
		if (documento == null) {
			ResponseEntity<Documento> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(documento);
			ResponseEntity<Documento> resposta = new ResponseEntity<Documento>(documento, HttpStatus.FOUND);
			return resposta;
		}

	}
	
	@GetMapping("/documentos")
	public ResponseEntity<List<Documento>> obterDocumentos() {
		List<Documento> documentos = documentoRepositorio.findAll();
		if (documentos.isEmpty()) {
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(documentos);
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(documentos, HttpStatus.FOUND);
			return resposta;
		}

	}
	
	@DeleteMapping("/documento/excluir/{id}")
	public ResponseEntity<Void> excluirDocumento(@PathVariable long id) {
	    HttpStatus status = HttpStatus.OK;
	    
	    java.util.Optional<Documento> documento = documentoRepositorio.findById(id);
	    
	    if (documento.isPresent()) {
	        documentoDeletor.excluirDocumentoCliente(id);
	    } else {
	        status = HttpStatus.NOT_FOUND;
	    }
	    
	    return new ResponseEntity<>(status);
	}
	
	@PostMapping("documento/cadastro/{id}")
	public ResponseEntity<?> cadastrarDocumento(@PathVariable long id,
	        @RequestBody Documento novoDocumento) {
	    HttpStatus status = HttpStatus.CREATED;

	    Cliente cliente = clienteRepositorio.findById(id).orElse(null);

	    if (cliente != null) {
	        documentoCadastro.cadastrarDocumento(cliente.getId(), novoDocumento);

	        Documento documento = documentoRepositorio.findById(novoDocumento.getId()).get();
	        adicionadorLink.adicionarLink(documento);
	    } else {
	        status = HttpStatus.NOT_FOUND;
	    }

	    return new ResponseEntity<>(status);
	}
    
	@PutMapping("documento/atualizar")
	@Transactional
	public ResponseEntity<?> atualizarDocumentos(@RequestBody DocumentoRecord atualizacoes) {
	    List<Documento> documentos = documentoRepositorio.findAll();
	    documentoAtualizador.atualizar(documentos, atualizacoes.documentos());

	    Link selfLink = Link.of("/documento/atualizar").withRel("self");

	    for (Documento documento : documentos) {
	        adicionadorLink.adicionarLink(documento, selfLink);
	    }

	    return documentos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().build();
	}
    
}
