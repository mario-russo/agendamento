package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.ports.in.DesativarFuncionarioUsecase;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioNotEmpresa;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioNotFound;

import java.util.Objects;

public class DesativarFuncionarioImpl implements DesativarFuncionarioUsecase {


    private final FuncionarioRepository funcionarioRepository;

    public DesativarFuncionarioImpl(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public void desativar(Long id, Long empresaId) {
        Funcionario funcionario = funcionarioRepository.buscaPorId(id);
        if (funcionario == null)
            throw new FuncionarioNotFound("Funcionário Não encontrado", 400);

        if (!Objects.equals(funcionario.getEmpresaId(), empresaId))
            throw new FuncionarioNotEmpresa("Funcionário não pertence a empresa", 403);

        funcionario.desativar();

        funcionarioRepository.atualizar(funcionario);

    }
}
