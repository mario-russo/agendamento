package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.ports.in.AtualizaFuncionarioUseCase;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioNotEmpresa;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioNotFound;
import jakarta.transaction.Transactional;

import java.util.Objects;

public class AtualizaFuncionarioImpl implements AtualizaFuncionarioUseCase {

    private final FuncionarioRepository funcionarioRepository;

    public AtualizaFuncionarioImpl(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    @Transactional
    public void atualizar(String nome, String telefone, Long id, Long empresaId) {
        Funcionario funcionario = funcionarioRepository.buscaPorId(id);
        if (funcionario == null)
            throw new FuncionarioNotFound("Funcionário Não encontrado", 400);

        if (!Objects.equals(funcionario.getEmpresaId(), empresaId))
            throw new FuncionarioNotEmpresa("Fucionário não pertence a empresa", 403);

        if (nome != null) {
            funcionario.alterarNome(nome);
        }

        if (telefone != null) {
            funcionario.alterarTelefone(telefone);
        }

        funcionarioRepository.atualizar(funcionario);

    }
}
