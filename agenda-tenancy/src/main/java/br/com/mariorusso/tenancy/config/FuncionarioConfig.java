package br.com.mariorusso.tenancy.config;

import br.com.mariorusso.tenancy.application.ports.in.BuscaFuncionarioUseCase;
import br.com.mariorusso.tenancy.application.ports.ou.FuncionarioRepository;
import br.com.mariorusso.tenancy.application.usecase.BuscaFuncionarioImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class FuncionarioConfig {

    @Inject
    FuncionarioRepository repository;


    @Produces
    @ApplicationScoped
    public BuscaFuncionarioUseCase buscaPorEmpresa (){
        return  new BuscaFuncionarioImpl(repository);
    }
}
