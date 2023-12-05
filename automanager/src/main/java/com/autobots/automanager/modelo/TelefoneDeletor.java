package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@Service
public class TelefoneDeletor {
	@Autowired
    private ClienteRepositorio clienteRepositorio;


    public void excluirTelefoneDeClientes(long id) {
        List<Cliente> clientes = clienteRepositorio.findAll();

        for (Cliente cliente : clientes) {
            List<Telefone> telefones = cliente.getTelefones();
            if (telefones != null) {
            	telefones.removeIf(telefone -> telefone.getId().equals(id));
            }
            clienteRepositorio.save(cliente);
        }
    }

}
