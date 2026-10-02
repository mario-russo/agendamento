package br.com.mariorusso.tenancy.config;

import br.com.mariorusso.tenancy.application.ports.in.AtualizaEmpresaUseCase;
import br.com.mariorusso.tenancy.application.ports.out.EmpresaRepository;
import br.com.mariorusso.tenancy.application.usecase.AtualizaEmpresaImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

public class EmpresaConfig {

    @Produces
    @ApplicationScoped

    public AtualizaEmpresaUseCase atualizaFuncionarioUseCase(EmpresaRepository empresaRepository){
        return new AtualizaEmpresaImpl(empresaRepository);

    }
}
