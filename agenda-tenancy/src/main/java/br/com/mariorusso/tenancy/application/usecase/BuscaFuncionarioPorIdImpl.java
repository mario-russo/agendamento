package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.ports.in.BuscaFuncionarioPorIdUseCase;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;

import java.util.Optional;

public class BuscaFuncionarioPorIdImpl implements BuscaFuncionarioPorIdUseCase{

    private final FuncionarioRepository repository;

    public BuscaFuncionarioPorIdImpl(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Funcionario> busca(Long id, Long empresaId) {
        return repository.buscaPorId(id)
                .filter(f -> f.getEmpresaId().equals(empresaId));

    }
}
