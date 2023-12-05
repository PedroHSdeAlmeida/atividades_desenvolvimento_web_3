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

import com.autobots.automanager.DTOs.TelefoneRecord;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.modelo.TelefoneCadastro;
import com.autobots.automanager.modelo.TelefoneDeletor;
import com.autobots.automanager.modelo.TelefoneSelecionador;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefone")
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
	
	
	@GetMapping("telefone/{id}")
	public Telefone obterTelefone(@PathVariable long id) {
		List<Telefone> telefones = telefoneRepositorio.findAll();
		return selecionar.selecionar(telefones, id);
	}
	
	@GetMapping("/telefones")
	public List<Telefone> obterTelefones() {
		List<Telefone> Telefones = telefoneRepositorio.findAll();
		return Telefones;
	}
	
	@DeleteMapping("/excluir/{id}")
	public void excluirTelefone(@PathVariable long id) {
		telefoneDeletor.excluirTelefoneDeClientes(id);
	    }
	
	@PostMapping("/cadastro/{id}")
    public void cadastrarTelefone(@PathVariable long id, @RequestBody Telefone novoTelefone) {
    	telefoneCadastro.cadastrarTelefone(id, novoTelefone);
    }
	
    @PutMapping("/atualizar")
    @Transactional
    public void atualizarTelefones(@RequestBody TelefoneRecord atualizacoes) {
        List<Telefone> telefones = telefoneRepositorio.findAll();
        telefoneAtualizador.atualizar(telefones, atualizacoes.telefones());
    }

}
