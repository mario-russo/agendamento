package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.ports.in.DesativarFuncionarioUsecase;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioDeOutraEmpresaException;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioNotFoundException;
import jakarta.transaction.Transactional;


public class DesativarFuncionarioImpl implements DesativarFuncionarioUsecase {


    private final FuncionarioRepository funcionarioRepository;

    public DesativarFuncionarioImpl(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    @Transactional
    public void desativar(Long id, Long empresaId) {

        Funcionario funcionario = funcionarioRepository.buscaPorId(id)
                .orElseThrow(() -> new FuncionarioNotFoundException("Funcionário Não encontrado"));

        if (!funcionario.getEmpresaId().equals(empresaId))
            throw new FuncionarioDeOutraEmpresaException("Funcionário não pertence a empresa");

        funcionario.desativar();

        funcionarioRepository.atualizar(funcionario);

    }
}
