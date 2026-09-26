package br.com.mariorusso.tenancy.application.ports.in;

import br.com.mariorusso.tenancy.application.dtos.request.FuncionarioRequestDto;

public interface SalvaFuncionarioUseCase {
    void execute(FuncionarioRequestDto dto);
}
