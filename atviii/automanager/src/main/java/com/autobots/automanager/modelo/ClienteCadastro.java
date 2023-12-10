package com.autobots.automanager.modelo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClienteCadastro {

	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@Autowired
	private TelefoneRepositorio telefoneRepositorio;

	@Transactional
	public Cliente cadastrarNovoCliente(Cliente cliente) {
	    validarCliente(cliente);

	    List<Telefone> telefonesSalvos = new ArrayList<>();
	    for (Telefone telefone : cliente.getTelefones()) {
	        if (!telefoneRepositorio.existsByNumero(telefone.getNumero())) {
	            telefone = telefoneRepositorio.save(telefone);
	        }
	        telefonesSalvos.add(telefone);
	    }

	    cliente.setTelefones(telefonesSalvos);

	    for (Documento documento : cliente.getDocumentos()) {
	        documento.setCliente(cliente);
	    }

	    cliente = clienteRepositorio.save(cliente);

	    return cliente;
	}

	private void validarCliente(Cliente cliente) {
		Cliente clienteExistente = clienteRepositorio.findOneByNome(cliente.getNome());
		if (clienteExistente != null && !clienteExistente.getId().equals(cliente.getId())) {
			throw new RuntimeException("Já existe um cliente com o mesmo nome.");
		}

	}


}
