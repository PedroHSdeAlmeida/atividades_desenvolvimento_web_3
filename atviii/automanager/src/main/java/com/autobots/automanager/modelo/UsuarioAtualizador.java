package com.autobots.automanager.modelo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

import java.util.Optional;
import java.util.Set;

@Component
public class UsuarioAtualizador {

	@Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private TelefoneRepositorio telefoneepositorio;
    @Autowired
    private DocumentoRepositorio documentoRepositorio;

    
    @Transactional
    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {

        Usuario usuarioExistente = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuarioExistente.setNome(Optional.ofNullable(usuarioAtualizado.getNome()).orElse(usuarioExistente.getNome()));
        atualizarColecao(usuarioAtualizado.getTelefones(), usuarioExistente.getTelefones());
        atualizarColecao(usuarioAtualizado.getDocumentos(), usuarioExistente.getDocumentos());
        return usuarioRepositorio.save(usuarioExistente);
    }

    private <T> void atualizarColecao(Set<T> novosItens, Set<T> colecaoExistente) {
        colecaoExistente.clear();
        Optional.ofNullable(novosItens).ifPresent(colecaoExistente::addAll);
    }
}
