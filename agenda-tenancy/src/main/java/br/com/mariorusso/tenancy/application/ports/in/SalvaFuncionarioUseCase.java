package br.com.mariorusso.tenancy.application.ports.in;

import br.com.mariorusso.tenancy.application.dtos.request.SalvaFuncionarioCommand;

public interface SalvaFuncionarioUseCase {
    void execute(SalvaFuncionarioCommand dto);
}
