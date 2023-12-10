package com.autobots.automanager.modelo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.enumeracoes.TipoVeiculo;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;

@Service
public class VeiculoCadastro {

    @Autowired
    private UsuarioRepositorio usuariorepositorio;
    
    @Autowired
    private VeiculoRepositorio veiculoRepositorio;

    public void cadastrarVeiculo(long Id, Veiculo novoVeiculo) {
        Usuario usuario= usuariorepositorio.getById(Id);

        if (usuario != null) {
        	novoVeiculo.setUsuario(usuario);

        	veiculoRepositorio.save(novoVeiculo);

            usuario.getVeiculos().add(novoVeiculo);

            usuariorepositorio.save(usuario);
        } else {
            throw new IllegalArgumentException("Usuario com ID " + Id + " não encontrado.");
        }
    }

	public TipoVeiculo getTipo() {
		return null;
	}
	public String getModelo() {
		return null;
	}
	public String getPlaca() {
		return null;
	}
	public Usuario getProprietario() {
		return null;
	}
}
