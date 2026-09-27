package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.dtos.Token;
import br.com.mariorusso.tenancy.application.ports.in.LoginUseCase;
import br.com.mariorusso.tenancy.application.ports.out.TokenGeneratorPort;
import br.com.mariorusso.tenancy.application.ports.out.UsuarioRepository;
import br.com.mariorusso.tenancy.domain.Usuario;
import br.com.mariorusso.tenancy.domain.exception.UsuarioNotFound;
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

        if (usuario == null) {
            throw new UsuarioNotFound("Usuário não encontrado");
        }

        if (!usuario.getPassword().equals(password)) {
             throw new UsuarioNotFound("Usuário não encontrado");
        }


        String tokenAccess = jwt.generateToken(usuario);
        String tokenRefresh = jwt.generateRefreshToken(usuario);
        Token token = new Token(tokenAccess, tokenRefresh);

        return token;
    }
}
