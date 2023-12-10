package com.autobots.automanager.modelo;

import com.autobots.automanager.entidades.Servico;
import lombok.Data;

@Data
public class ServicosCadastro {
    private String nome;
    private double valor;
    private String descricao;

    public Servico toServico() {
        Servico servico = new Servico();
        servico.setNome(this.nome);
        servico.setValor(this.valor);
        servico.setDescricao(this.descricao);
        return servico;
    }
}
