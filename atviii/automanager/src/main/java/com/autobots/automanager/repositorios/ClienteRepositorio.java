package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entidades.Cliente;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

    Cliente findOneByNome(String nome);

}
