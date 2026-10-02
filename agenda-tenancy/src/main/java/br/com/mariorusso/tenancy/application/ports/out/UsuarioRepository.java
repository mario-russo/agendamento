package br.com.mariorusso.tenancy.application.ports.out;

import br.com.mariorusso.tenancy.application.command.UsuarioRequest;
import br.com.mariorusso.tenancy.domain.Usuario;

public interface UsuarioRepository {
    Usuario buscaPorEmail(String email);

    Usuario buscaPorId(Long id);

    void salvarUsuario(UsuarioRequest request);
}
