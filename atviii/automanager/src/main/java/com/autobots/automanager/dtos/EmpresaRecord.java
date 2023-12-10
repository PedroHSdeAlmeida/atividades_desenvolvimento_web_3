package com.autobots.automanager.dtos;

import com.autobots.automanager.entidades.Empresa;

public record EmpresaRecord(Empresa empresa) {

  public Empresa toEmpresa() {
    return this.empresa;
  }

}
