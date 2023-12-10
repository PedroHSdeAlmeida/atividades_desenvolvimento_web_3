package com.autobots.automanager.modelo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@Service
public class DocumentoCadastro {

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private DocumentoRepositorio documentoRepositorio;

    public void cadastrarDocumento(long Id, Documento novoDocumento) {
        Cliente cliente = clienteRepositorio.getById(Id);

        if (cliente != null) {
            novoDocumento.setCliente(cliente);

            documentoRepositorio.save(novoDocumento);

            cliente.getDocumentos().add(novoDocumento);

            clienteRepositorio.save(cliente);
        } else {
            throw new IllegalArgumentException("Cliente com ID " + Id + " não encontrado.");
        }
    }
}
