package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.command.request.AtualizaEmpresaCommand;
import br.com.mariorusso.tenancy.application.ports.in.AtualizaEmpresaUseCase;
import br.com.mariorusso.tenancy.application.ports.out.EmpresaRepository;
import br.com.mariorusso.tenancy.domain.Empresa;
import br.com.mariorusso.tenancy.domain.exception.EmpresaNotFound;
import jakarta.transaction.Transactional;


public class AtualizaEmpresaImpl implements AtualizaEmpresaUseCase {

    private final EmpresaRepository empresaRepository;

    public AtualizaEmpresaImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    @Transactional
    public void atualizar(Long empresaId, AtualizaEmpresaCommand command) {
        Empresa empresa = empresaRepository.buscaPorId(empresaId)
                .orElseThrow(() -> new EmpresaNotFound("Empresa não encontrada"));



        if (command.name() != null) empresa.alteraName(command.name());
        if (command.cnpj() != null) empresa.alteraCnpj(command.cnpj());
        if (command.email() != null) empresa.alteraEmail(command.email());
        if (command.telefone() != null) empresa.alteraTelefone(command.telefone());
        if (command.endereco() != null || command.municipio() != null || command.estado() != null) {
            empresa.alteraEndereco(command.endereco(), command.municipio(), command.estado());
        }

        empresaRepository.atualizar(empresa);

    }
}
