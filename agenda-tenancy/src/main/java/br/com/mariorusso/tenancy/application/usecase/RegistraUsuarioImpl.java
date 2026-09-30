package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.adapters.outbound.entity.EmpresaEntity;
import br.com.mariorusso.tenancy.adapters.outbound.entity.UsuarioEntity;
import br.com.mariorusso.tenancy.application.command.UsuarioRequest;
import br.com.mariorusso.tenancy.application.ports.in.RegistrarUsuarioUseCase;
import br.com.mariorusso.tenancy.application.ports.out.UsuarioRepository;
import jakarta.transaction.Transactional;

public class RegistraUsuarioImpl implements RegistrarUsuarioUseCase {

    private final UsuarioRepository repository;

    public RegistraUsuarioImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    //Ao fazer um registro do usuario uma empresa é criada junta

    @Override
    @Transactional
    public void registrar(UsuarioRequest request) {
        UsuarioEntity usuarioEntity = UsuarioEntity.fromDomain(request.toDomain());
        EmpresaEntity empresaEntity = new EmpresaEntity();

        usuarioEntity.persist();

        empresaEntity.usuarioId = usuarioEntity.id;

        empresaEntity.persist();

    }
}
