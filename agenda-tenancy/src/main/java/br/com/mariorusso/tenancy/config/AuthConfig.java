package br.com.mariorusso.tenancy.config;

import br.com.mariorusso.tenancy.application.ports.in.BuscaFuncionarioUseCase;
import br.com.mariorusso.tenancy.application.ports.in.LoginUseCase;
import br.com.mariorusso.tenancy.application.ports.in.RegistrarUsuarioUseCase;
import br.com.mariorusso.tenancy.application.ports.ou.TokenGeneratorPort;
import br.com.mariorusso.tenancy.application.ports.ou.UsuarioRepository;
import br.com.mariorusso.tenancy.application.usecase.BuscaFuncionarioImpl;
import br.com.mariorusso.tenancy.application.usecase.LoginService;
import br.com.mariorusso.tenancy.application.usecase.RegistraUsuarioImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthConfig {

    @Inject
    TokenGeneratorPort tokenGeneratorPort;

    @Inject
    UsuarioRepository usuarioRepository;

    @Produces
    @ApplicationScoped
    public LoginUseCase loginUseCase (){
        return  new LoginService(tokenGeneratorPort,usuarioRepository);
    }
    @Produces
    @ApplicationScoped
    public RegistrarUsuarioUseCase registraUsuario (){
        return  new RegistraUsuarioImpl(usuarioRepository);
    }




}
