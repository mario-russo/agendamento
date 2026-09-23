package br.com.mariorusso.tenancy.application.ports.ou;

import br.com.mariorusso.tenancy.application.dtos.UsuarioRequest;
import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario buscaPorEmail(String email);

    Usuario buscaPorId(Long id);

    void salvarUsuario(UsuarioRequest request);
}
