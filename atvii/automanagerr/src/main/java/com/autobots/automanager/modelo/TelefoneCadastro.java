package com.autobots.automanager.modelo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@Service
public class TelefoneCadastro {
	
	@Autowired
    private ClienteRepositorio clienteRepositorio;
	@Autowired
	private TelefoneRepositorio telefoneRepositorio;
	
    public void cadastrarTelefone(long Id, Telefone novoTelefone) {
        Cliente cliente = clienteRepositorio.getById(Id);

        if (cliente != null) {
        	novoTelefone.setCliente(cliente);

        	telefoneRepositorio.save(novoTelefone);

            cliente.getTelefones().add(novoTelefone);

            clienteRepositorio.save(cliente);
        } else {
            throw new IllegalArgumentException("Cliente com ID " + Id + " não encontrado.");
        }
    }


}
