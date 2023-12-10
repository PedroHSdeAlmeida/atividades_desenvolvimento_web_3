package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.autobots.automanager.dtos.EmpresaRecord;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.modelo.AdicionadorLinkEmpresa;
import com.autobots.automanager.modelo.EmpresaAtualizacao;
import com.autobots.automanager.modelo.EmpresaSelecionador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;

@RestController
public class EmpresaControle {
    
    @Autowired
    private EmpresaAtualizacao empresaAtualizacao;
    @Autowired
    private EmpresaRepositorio repositorio;
    @Autowired
    private EmpresaSelecionador selecionador;
    @Autowired
    private AdicionadorLinkEmpresa adicionadorLink;



    
    
	@GetMapping("/empresa/{id}")
	public ResponseEntity<Empresa> obterEmpresa(@PathVariable long id) {
		List<Empresa> empresas = repositorio.findAll();
		Empresa empresa = selecionador.selecionar(empresas, id);
		if (empresa == null) {
			ResponseEntity<Empresa> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(empresa);
			ResponseEntity<Empresa> resposta = new ResponseEntity<Empresa>(empresa, HttpStatus.FOUND);
			return resposta;
		}
	}
    
	@GetMapping("/empresas")
	public ResponseEntity<List<Empresa>> obterEmpresas() {
		List<Empresa> empresas = repositorio.findAll();
		if (empresas.isEmpty()) {
			ResponseEntity<List<Empresa>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(empresas);
			ResponseEntity<List<Empresa>> resposta = new ResponseEntity<>(empresas, HttpStatus.FOUND);
			return resposta;
		}
	}
    

	@PostMapping("/empresa/cadastro")
	public ResponseEntity<?> cadastrarClientEmpresa(@RequestBody Empresa empresa) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (empresa.getId() == null) {
			repositorio.save(empresa);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);

	}
    
	@PutMapping("/empresa/{id}")
	public ResponseEntity<EntityModel<Empresa>> atualizarEmpresa(@PathVariable Long id, @RequestBody EmpresaRecord empresaRecord) {
	  Empresa empresaExistente = repositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	  Empresa empresaAtualizada = empresaAtualizacao.atualizarEmpresa(empresaExistente, empresaRecord.toEmpresa());
	  return ResponseEntity.ok(EntityModel.of(empresaAtualizada));
	}


    @DeleteMapping("/empresa/{id}")
    public ResponseEntity<?> excluirEmpresa(@PathVariable Long id) {
        Optional<Empresa> empresaOptional = repositorio.findById(id);

        if (empresaOptional.isPresent()) {
            Empresa empresa = empresaOptional.get();
            repositorio.delete(empresa);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
