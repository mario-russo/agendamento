package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.ports.in.AtualizaFuncionarioUseCase;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioDeOutraEmpresaException;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioNotFoundException;
import jakarta.transaction.Transactional;


public class AtualizaFuncionarioImpl implements AtualizaFuncionarioUseCase {

    private final FuncionarioRepository funcionarioRepository;

    public AtualizaFuncionarioImpl(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    @Transactional
    public void atualizar(Long id, Long empresaId, String nome, String telefone) {
        Funcionario funcionario = funcionarioRepository.buscaPorId(id)
                .orElseThrow(() -> new FuncionarioNotFoundException("Funcionário não encontrado"));

        if (!funcionario.getEmpresaId().equals(empresaId)) {
            throw new FuncionarioDeOutraEmpresaException("Funcionário não pertence à empresa");
        }

        if (nome != null) {
            funcionario.alterarNome(nome);
        }

        if (telefone != null) {
            funcionario.alterarTelefone(telefone);
        }

        funcionarioRepository.atualizar(funcionario);
    }
}
