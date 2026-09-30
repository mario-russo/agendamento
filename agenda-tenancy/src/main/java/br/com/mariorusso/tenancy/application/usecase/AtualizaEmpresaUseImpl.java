package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.share.Endereco;
import br.com.mariorusso.tenancy.application.command.request.AtualizaEmpresaCommand;
import br.com.mariorusso.tenancy.application.ports.in.AtualizaEmpresaUseCase;
import br.com.mariorusso.tenancy.application.ports.out.EmpresaRepository;
import br.com.mariorusso.tenancy.domain.Empresa;
import jakarta.transaction.Transactional;

public class AtualizaEmpresaUseImpl implements AtualizaEmpresaUseCase {

    private final EmpresaRepository empresaRepository;

    public AtualizaEmpresaUseImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    @Transactional
    public void atualizar(Long empresaId, AtualizaEmpresaCommand command) {

        AtualizaEmpresaCommand empresaCommand = AtualizaEmpresaCommand.comId(empresaId, command);

        Endereco endereco = new Endereco(empresaCommand.endereco(), empresaCommand.municipio(), empresaCommand.estado());
        Empresa empresa = new Empresa(
                empresaCommand.name(),
                empresaCommand.cnpj(),
                empresaCommand.email(),
                empresaCommand.telefone(),
                endereco,
                empresaCommand.usuarioId()
                );

        empresaRepository.atualizar(empresa);

    }
}
