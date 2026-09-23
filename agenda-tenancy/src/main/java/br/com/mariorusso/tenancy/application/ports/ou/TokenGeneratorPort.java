package br.com.mariorusso.tenancy.application.ports.ou;

import br.com.mariorusso.tenancy.domain.Usuario;

public interface TokenGeneratorPort {

    String generateToken(Usuario usuario);

    String generateRefreshToken(Usuario usuario);

}
