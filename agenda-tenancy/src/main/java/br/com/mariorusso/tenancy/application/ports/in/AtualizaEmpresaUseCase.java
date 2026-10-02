package br.com.mariorusso.tenancy.application.ports.in;

import br.com.mariorusso.tenancy.application.command.request.AtualizaEmpresaCommand;

public interface AtualizaEmpresaUseCase {

    void atualizar(Long empresaId, AtualizaEmpresaCommand empresa);
}
