package com.autobots.automanager.modelo;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@Service
public class VeiculoDeletor {

    @Autowired
    private UsuarioRepositorio repositorio;

    public void excluirVeiculo(long veiculoId) {
        List<Usuario> usuarios = repositorio.findAll();

        for (Usuario usuario: usuarios) {
            Set<Veiculo> veiculos = usuario.getVeiculos();
            if (veiculos != null) {
                veiculos.removeIf(veiculo -> veiculo.getId().equals(veiculoId));
            }
            repositorio.save(usuario);
        }
    }
}
