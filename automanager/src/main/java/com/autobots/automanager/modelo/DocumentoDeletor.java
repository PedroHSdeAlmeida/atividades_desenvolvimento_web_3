package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@Service
public class DocumentoDeletor {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public void excluirDocumentoDeClientes(long documentoId) {
        List<Cliente> clientes = clienteRepositorio.findAll();

        for (Cliente cliente : clientes) {
            List<Documento> documentos = cliente.getDocumentos();
            if (documentos != null) {
                documentos.removeIf(documento -> documento.getId().equals(documentoId));
            }
            clienteRepositorio.save(cliente);
        }
    }
}
