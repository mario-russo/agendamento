package br.com.mariorusso.tenancy.application.ports.in;

import br.com.mariorusso.tenancy.domain.Funcionario;

import java.util.List;

public interface BuscaFuncionarioUseCase {
    List<Funcionario> buscaPorEmpresa(Long empresaId);
}
