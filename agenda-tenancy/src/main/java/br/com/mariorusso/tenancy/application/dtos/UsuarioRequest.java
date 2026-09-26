package br.com.mariorusso.tenancy.application.dtos;

import br.com.mariorusso.tenancy.domain.Usuario;
import br.com.mariorusso.tenancy.domain.valueobject.RoleEnum;
import br.com.mariorusso.tenancy.domain.valueobject.TypeEnum;

import java.util.Set;

public record UsuarioRequest(

        String name,
        String email,
        String password,
        Set<RoleEnum> role,
        TypeEnum tipo
) {
    public Usuario toDomain() {
        return new Usuario(
                email,
                password,
                name,
                role,
                tipo
        );
    }
}
