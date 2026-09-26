package br.com.mariorusso.tenancy.application.ports.out;

import br.com.mariorusso.tenancy.application.dtos.request.EmpresaRequest;
import br.com.mariorusso.tenancy.domain.Empresa;

public interface EmpresaRepository {
     void cadastra(EmpresaRequest request);
     Empresa buscaPorId(Long id);
}
