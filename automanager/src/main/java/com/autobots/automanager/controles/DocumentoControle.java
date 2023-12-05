package com.autobots.automanager.controles;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.DTOs.DocumentoRecord;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.DocumentoCadastro;
import com.autobots.automanager.modelo.DocumentoDeletor;
import com.autobots.automanager.modelo.DocumentoSelecionador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
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


	
	@GetMapping("documento/{id}")
	public Documento obterDocumento(@PathVariable long id) {
		List<Documento> documentos = documentoRepositorio.findAll();
		return selecionador.selecionar(documentos, id);
	}
	
	@GetMapping("/documentos")
	public List<Documento> obterDocumentos() {
		List<Documento> Documentos = documentoRepositorio.findAll();
		return Documentos;
	}
	
	@DeleteMapping("/excluir/{id}")
	public void excluirDocumento(@PathVariable long id) {
        documentoDeletor.excluirDocumentoDeClientes(id);
	    }
	
    @PostMapping("/cadastro/{id}")
    public void cadastrarDocumento(@PathVariable long id,
    		@RequestBody Documento novoDocumento) {

    	documentoCadastro.cadastrarDocumento(id, novoDocumento);
    }
    
    @PutMapping("/atualizar")
    @Transactional
    public void atualizarDocumentos(@RequestBody DocumentoRecord atualizacoes) {
        List<Documento> documentos = documentoRepositorio.findAll();
        documentoAtualizador.atualizar(documentos, atualizacoes.documentos());
    }
    
}
