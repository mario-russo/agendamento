package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.dtos.Token;
import br.com.mariorusso.tenancy.application.ports.in.LoginUseCase;
import br.com.mariorusso.tenancy.application.ports.ou.TokenGeneratorPort;
import br.com.mariorusso.tenancy.application.ports.ou.UsuarioRepository;
import br.com.mariorusso.tenancy.domain.Usuario;
import jakarta.ws.rs.NotFoundException;

public class LoginService implements LoginUseCase {
    private final TokenGeneratorPort jwt;
    private final UsuarioRepository usuarioRepository;

    public LoginService(TokenGeneratorPort jwt, UsuarioRepository repository) {
        this.jwt = jwt;
        this.usuarioRepository = repository;
    }

    @Override
    public Token exec(String email, String password) {
        Usuario usuario = usuarioRepository.buscaPorEmail(email);

        if (!usuario.getPassword().equals(password) || usuario == null)
            throw new NotFoundException("Usuario não econtrado");


        String tokenAccess = jwt.generateToken(usuario);
        String tokenRefresh = jwt.generateRefreshToken(usuario);
        Token token = new Token(tokenAccess, tokenRefresh);

        return token;
    }
}
