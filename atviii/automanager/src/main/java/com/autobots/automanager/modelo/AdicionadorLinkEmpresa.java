package com.autobots.automanager.modelo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.entidades.Empresa;

import java.util.List;

@Component
public class AdicionadorLinkEmpresa implements AdicionadorLink<Empresa> {

    @Override
    public void adicionarLink(List<Empresa> lista) {
        for (Empresa empresa : lista) {
            Long id = empresa.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(EmpresaControle.class)
                            .obterEmpresa(id))  
                    .withSelfRel();
            empresa.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(Empresa objeto) {
        Long id = objeto.getId();
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaControle.class)
                        .obterEmpresa(id)) 
                .withRel("empresas");
        objeto.add(linkProprio);
    }
}
