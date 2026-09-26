package br.com.mariorusso.tenancy.application.ports.in;

import br.com.mariorusso.tenancy.application.dtos.UsuarioRequest;

public interface RegistrarUsuarioUseCase {

    void registrar(UsuarioRequest request);
}
