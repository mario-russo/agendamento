package br.com.mariorusso.tenancy.application.ports.out;

import br.com.mariorusso.tenancy.application.command.request.AtualizaEmpresaCommand;
import br.com.mariorusso.tenancy.application.command.request.EmpresaRequest;
import br.com.mariorusso.tenancy.domain.Empresa;

import java.util.Optional;

public interface EmpresaRepository {

     Optional<Empresa> buscaPorId(Long id);
     void atualizar(Empresa empresa);
}
