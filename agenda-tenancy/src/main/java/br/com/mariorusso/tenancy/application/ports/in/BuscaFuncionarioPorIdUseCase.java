package br.com.mariorusso.tenancy.application.ports.in;

import br.com.mariorusso.tenancy.domain.Funcionario;

import java.util.Optional;

public interface BuscaFuncionarioPorIdUseCase {
    Optional<Funcionario> busca(Long id, Long empresaId );
}
