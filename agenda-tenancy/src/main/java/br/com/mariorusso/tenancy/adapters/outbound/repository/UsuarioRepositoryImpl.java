package br.com.mariorusso.tenancy.adapters.outbound.repository;

import br.com.mariorusso.tenancy.adapters.outbound.entity.UsuarioEntity;
import br.com.mariorusso.tenancy.application.command.UsuarioRequest;
import br.com.mariorusso.tenancy.application.ports.out.UsuarioRepository;
import br.com.mariorusso.tenancy.domain.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class UsuarioRepositoryImpl implements UsuarioRepository {
    UsuarioEntity usuarioEntity;

    @Override
    public Usuario buscaPorEmail(String email) {
        UsuarioEntity entity = usuarioEntity.find("email.value", email).firstResult();
        if (entity == null)
            return null;
        return entity.toDomain();
    }

    @Override
    public Usuario buscaPorId(Long id) {
        UsuarioEntity entity = usuarioEntity.find("id", id).firstResult();
        return entity.toDomain();
    }

    @Override
    @Transactional
    public void salvarUsuario(UsuarioRequest request) {
        UsuarioEntity entity = UsuarioEntity.fromDomain(request.toDomain());

        entity.persist();

    }
}
