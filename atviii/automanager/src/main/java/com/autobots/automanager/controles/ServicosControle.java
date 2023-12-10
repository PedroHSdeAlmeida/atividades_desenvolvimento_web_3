package com.autobots.automanager.controles;

import com.autobots.automanager.repositorios.ServicosRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.modelo.AdicionarLinkServicos;
import com.autobots.automanager.modelo.ServicosCadastro;

import org.springframework.hateoas.CollectionModel;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ServicosControle {
	
	@Autowired
	private ServicosRepositorio repositorio;
	@Autowired
	private AdicionarLinkServicos adicionadorLinkServicos;
	
	@PostMapping
	public ResponseEntity<EntityModel<Servico>> cadastrarServico(@RequestBody ServicosCadastro servicoCadastro) {
	    Servico novoServico = servicoCadastro.toServico();

	    if (novoServico.isValid()) {
	        Servico servicoCadastrado = repositorio.save(novoServico);
	        Link selfLink = WebMvcLinkBuilder
	                .linkTo(WebMvcLinkBuilder.methodOn(ServicosControle.class).obterServicoPorId(servicoCadastrado.getId()))
	                .withSelfRel();
	        EntityModel<Servico> resource = EntityModel.of(servicoCadastrado, selfLink);
	        return new ResponseEntity<>(resource, HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}

	@GetMapping("/servico/{id}")
	public ResponseEntity<EntityModel<Servico>> obterServicoPorId(@PathVariable Long id) {
		Servico servico = repositorio.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

		adicionadorLinkServicos.adicionarLink(servico);

		Link selfLink = Link.of(String.format("/servicos/%d", servico.getId())).withSelfRel();
		EntityModel<Servico> resource = EntityModel.of(servico, selfLink);

		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	@GetMapping("/servicos")
	public CollectionModel<EntityModel<Servico>> obterServicos() {
		List<Servico> servicos = repositorio.findAll();
		List<EntityModel<Servico>> servicosDTO = servicos.stream().map(servico -> {
			adicionadorLinkServicos.adicionarLink(servico);
			Link selfLink = Link.of(String.format("/servicos/%d", servico.getId())).withSelfRel();
			return EntityModel.of(servico, selfLink);
		}).collect(Collectors.toList());
		Link linkSelf = Link.of("/servicos").withSelfRel();
		Link linkPost = Link.of("/servicos").withRel("post");
		return CollectionModel.of(servicosDTO, linkSelf, linkPost);
	}

	@PutMapping("/servico/{id}")
	public ResponseEntity<EntityModel<Servico>> atualizarServico(@PathVariable Long id,
	                                                         @RequestBody ServicosCadastro servicoCadastro) {
	    java.util.Optional<Servico> servicoOptional = repositorio.findById(id);

	    if (servicoOptional.isPresent()) {
	        Servico servico = servicoOptional.get();

	        servico.setNome(servicoCadastro.getNome());
	        servico.setValor(servicoCadastro.getValor());
	        servico.setDescricao(servicoCadastro.getDescricao());

	        if (servico.isValid()) {
	            Servico servicoAtualizado = repositorio.save(servico);

	            adicionadorLinkServicos.adicionarLink(servicoAtualizado);

	            Link selfLink = Link.of(String.format("/servicos/%d", servicoAtualizado.getId())).withSelfRel();
	            EntityModel<Servico> resource = EntityModel.of(servicoAtualizado, selfLink);

	            return new ResponseEntity<>(resource, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        }
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@DeleteMapping("/servico/{id}")
	public ResponseEntity<?> excluirServico(@PathVariable Long id) {
	    if (repositorio.existsById(id)) {
	        repositorio.deleteById(id);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

}
