package com.autobots.automanager.controles;

import com.autobots.automanager.repositorios.MercadoriaRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelo.AdicionarLinkMercadoria;
import com.autobots.automanager.modelo.MercadoriaCadastro;

import org.springframework.hateoas.CollectionModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MercadoriaControle {

    @Autowired
    private MercadoriaRepositorio repositorio;

    @Autowired
    private AdicionarLinkMercadoria adicionadorLinkMercadorias;

    @PostMapping
    public ResponseEntity<EntityModel<Mercadoria>> cadastrarMercadoria(@RequestBody MercadoriaCadastro mercadoriaCadastro) {
        try {
            Mercadoria novaMercadoria = mercadoriaCadastro.toMercadoria();

            Mercadoria mercadoriaCadastrada = repositorio.save(novaMercadoria);

            adicionadorLinkMercadorias.adicionarLink(mercadoriaCadastrada);

            EntityModel<Mercadoria> resource = EntityModel.of(mercadoriaCadastrada);

            return new ResponseEntity<>(resource, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/mercadoria/{id}")
    public ResponseEntity<EntityModel<Mercadoria>> obterMercadoria(@PathVariable Long id) {
        Optional<Mercadoria> optionalMercadoria = repositorio.findById(id);
        if (optionalMercadoria.isPresent()) {
            Mercadoria mercadoria = optionalMercadoria.get();

            adicionadorLinkMercadorias.adicionarLink(mercadoria);

            Link selfLink = Link.of(String.format("/mercadorias/%d", mercadoria.getId())).withSelfRel();
            EntityModel<Mercadoria> resource = EntityModel.of(mercadoria, selfLink);

            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public CollectionModel<EntityModel<Mercadoria>> obterMercadorias() {
        List<Mercadoria> mercadorias = repositorio.findAll();
        List<EntityModel<Mercadoria>> mercadoriasDTO = mercadorias.stream()
                .map(mercadoria -> {
                    adicionadorLinkMercadorias.adicionarLink(mercadoria);
                    return EntityModel.of(mercadoria);
                })
                .collect(Collectors.toList());

        Link linkSelf = Link.of("/mercadorias").withSelfRel();
        Link linkPost = Link.of("/mercadorias").withRel("post");

        return CollectionModel.of(mercadoriasDTO, linkSelf, linkPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Mercadoria>> atualizarMercadoria(@PathVariable Long id, @RequestBody MercadoriaCadastro mercadoriaCadastro) {
        try {
            Optional<Mercadoria> optionalMercadoria = repositorio.findById(id);
            if (optionalMercadoria.isPresent()) {
                Mercadoria mercadoriaExistente = optionalMercadoria.get();

                mercadoriaExistente.setNome(mercadoriaCadastro.getNome());
                mercadoriaExistente.setQuantidade(mercadoriaCadastro.getQuantidade());
                mercadoriaExistente.setValor(mercadoriaCadastro.getValor());
                mercadoriaExistente.setDescricao(mercadoriaCadastro.getDescricao());

                Mercadoria mercadoriaAtualizada = repositorio.save(mercadoriaExistente);

                adicionadorLinkMercadorias.adicionarLink(mercadoriaAtualizada);

                Link selfLink = Link.of(String.format("/mercadorias/%d", mercadoriaAtualizada.getId())).withSelfRel();
                EntityModel<Mercadoria> resource = EntityModel.of(mercadoriaAtualizada, selfLink);

                return new ResponseEntity<>(resource, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirMercadoria(@PathVariable Long id) {
        if (repositorio.existsById(id)) {
        	repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
