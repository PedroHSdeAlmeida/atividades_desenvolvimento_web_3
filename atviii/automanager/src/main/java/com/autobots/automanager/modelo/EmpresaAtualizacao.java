package com.autobots.automanager.modelo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

import java.util.Set;

@Component
public class EmpresaAtualizacao {

	@Autowired
	private EmpresaRepositorio repositorio;

	@Autowired
	private TelefoneRepositorio telefoneRepositorio;

	@Transactional
	public Empresa atualizarEmpresa(Empresa empresaExistente, Empresa empresaAtualizada) {
		validarEmpresa(empresaAtualizada);

		empresaExistente.setRazaoSocial(empresaAtualizada.getRazaoSocial());
		empresaExistente.setNomeFantasia(empresaAtualizada.getNomeFantasia());
		empresaExistente.setCadastro(empresaAtualizada.getCadastro());
		empresaExistente.setEndereco(empresaAtualizada.getEndereco());

		Set<Telefone> telefonesExistente = empresaExistente.getTelefones();

		Set<Telefone> telefonesAtualizados = empresaAtualizada.getTelefones();

		for (Telefone telefone : telefonesAtualizados) {
			if (!telefonesExistente.contains(telefone)) {
				telefone = telefoneRepositorio.save(telefone);
			}
		}

		telefonesExistente.clear();
		telefonesExistente.addAll(telefonesAtualizados);

		empresaExistente = repositorio.save(empresaExistente);

		return empresaExistente;
	}

	private void validarEmpresa(Empresa empresa) {
		if (empresa.getRazaoSocial() == null || empresa.getRazaoSocial().trim().isEmpty()) {
			throw new RuntimeException("Razão social obrigatoria");
		}
	}
}
