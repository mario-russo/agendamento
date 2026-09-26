package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.ports.in.BuscaFuncionarioUseCase;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

public class BuscaFuncionarioImpl implements BuscaFuncionarioUseCase {

    private final FuncionarioRepository repository;

    public BuscaFuncionarioImpl(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Funcionario> buscaPorEmpresa(Long empresaId) {

        List<Funcionario> funcionarios = repository.buscaPorEmpresa(empresaId);

        if (funcionarios == null || funcionarios.isEmpty())
            throw new NotFoundException("usuario não encontrado");

        boolean funcionarioDaEmpresa = funcionarios
                .stream().allMatch(
                        funcionario -> funcionario.getEmpresaId().equals(empresaId));

        if (funcionarioDaEmpresa == false)
            throw new IllegalArgumentException("Erro ao lista usuário");

        return funcionarios;
    }
}
