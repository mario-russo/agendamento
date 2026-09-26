package br.com.mariorusso.tenancy.config;

import br.com.mariorusso.tenancy.application.ports.in.LoginUseCase;
import br.com.mariorusso.tenancy.application.ports.in.RegistrarUsuarioUseCase;
import br.com.mariorusso.tenancy.application.ports.out.TokenGeneratorPort;
import br.com.mariorusso.tenancy.application.ports.out.UsuarioRepository;
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
