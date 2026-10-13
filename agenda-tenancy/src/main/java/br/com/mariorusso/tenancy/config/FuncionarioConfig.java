package br.com.mariorusso.tenancy.config;

import br.com.mariorusso.tenancy.application.ports.in.*;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.application.usecase.*;
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

    @Produces
    @ApplicationScoped
    public SalvaFuncionarioUseCase salvaFuncionario (){
        return  new SalvaFuncionarioImpl(repository);
    }

    @Produces
    @ApplicationScoped
    public AtualizaFuncionarioUseCase atualizaFuncionarioUseCase (){
        return  new AtualizaFuncionarioImpl(repository);
    }

    @Produces
    @ApplicationScoped
    public DesativarFuncionarioUsecase desativarFuncionarioUsecase (){
        return  new DesativarFuncionarioImpl(repository);
    }

    @Produces
    @ApplicationScoped
    public BuscaFuncionarioPorIdUseCase buscaFuncionarioPorIdUseCase (){
        return  new BuscaFuncionarioPorIdImpl(repository);
    }
}
