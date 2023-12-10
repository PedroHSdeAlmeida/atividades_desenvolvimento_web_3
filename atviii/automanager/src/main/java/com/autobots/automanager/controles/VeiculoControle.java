package com.autobots.automanager.controles;

import com.autobots.automanager.repositorios.VeiculoRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelo.AdicionarLinkVeiculos;
import com.autobots.automanager.modelo.VeiculoCadastro;
import com.autobots.automanager.modelo.VeiculoDeletor;
import com.autobots.automanager.modelo.VeiculoSelecionador;

import org.springframework.hateoas.CollectionModel;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VeiculoControle {

	@Autowired
	private VeiculoDeletor veiculodeletor;
	@Autowired
	private VeiculoSelecionador selecionar;
	@Autowired
	private VeiculoCadastro veiculoCadastro;
	@Autowired
	private VeiculoRepositorio repositorio;
	@Autowired
	private AdicionarLinkVeiculos adicionadorLinkVeiculos;

	@PostMapping("veiculo/cadastro/{id}")
	public ResponseEntity<?> cadastrarVeiculo(@PathVariable long id,
	        @RequestBody Veiculo novoVeiculo) {
	    HttpStatus status = HttpStatus.CREATED;

	    Veiculo veiculo= repositorio.findById(id).orElse(null);

	    if (veiculo != null) {
	    	veiculoCadastro.cadastrarVeiculo(id, novoVeiculo);

	        Veiculo veiculo1= repositorio.findById(novoVeiculo.getId()).get();
	        adicionadorLinkVeiculos.adicionarLink(veiculo1);
	    } else {
	        status = HttpStatus.NOT_FOUND;
	    }

	    return new ResponseEntity<>(status);
	}

	@GetMapping("/veiculo/{id}")
	public ResponseEntity<Veiculo> obterVeiculo(@PathVariable long id) {
		List<Veiculo> veiculos= repositorio.findAll();
		Veiculo veiculo= selecionar.selecionar(veiculos, id);
		if (veiculo == null) {
			ResponseEntity<Veiculo> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLinkVeiculos.adicionarLink(veiculo);
			ResponseEntity<Veiculo> resposta = new ResponseEntity<Veiculo>(veiculo, HttpStatus.FOUND);
			return resposta;
		}

	}

	@GetMapping("/veiculos")
	public ResponseEntity<List<Veiculo>> obterVeiculos() {
		List<Veiculo> veiculos = repositorio.findAll();
		if (veiculos.isEmpty()) {
			ResponseEntity<List<Veiculo>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLinkVeiculos.adicionarLink(veiculos);
			ResponseEntity<List<Veiculo>> resposta = new ResponseEntity<>(veiculos, HttpStatus.FOUND);
			return resposta;
		}

	}

	@PutMapping("/veiculo/atualizar/{id}")
	public ResponseEntity<EntityModel<Veiculo>> atualizarVeiculo(@PathVariable Long id,
			@RequestBody VeiculoCadastro veiculoCadastro) {
		Veiculo veiculo = repositorio.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado"));

		veiculo.setTipo(veiculoCadastro.getTipo());
		veiculo.setModelo(veiculoCadastro.getModelo());
		veiculo.setPlaca(veiculoCadastro.getPlaca());
		veiculo.setProprietario(veiculoCadastro.getProprietario());

		Veiculo veiculoAtualizado = repositorio.save(veiculo);
		adicionadorLinkVeiculos.adicionarLink(veiculoAtualizado);
		Link selfLink = Link.of(String.format("/veiculos/%d", veiculoAtualizado.getId())).withSelfRel();
		EntityModel<Veiculo> resource = EntityModel.of(veiculoAtualizado).add(selfLink);

		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	@DeleteMapping("/veiculo/excluir/{id}")
	public ResponseEntity<Void> excluirVeiculo(@PathVariable long id) {
	    HttpStatus status = HttpStatus.OK;
	    
	    java.util.Optional<Veiculo> veiculo = repositorio.findById(id);
	    
	    if (veiculo.isPresent()) {
	    	veiculodeletor.excluirVeiculo(id);
	    } else {
	        status = HttpStatus.NOT_FOUND;
	    }
	    
	    return new ResponseEntity<>(status);
	}

}
