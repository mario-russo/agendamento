package br.com.mariorusso.tenancy.adapters.outbound.repository;


import br.com.mariorusso.tenancy.adapters.outbound.entity.FuncionarioEntity;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FuncionarioRepositoryImpl implements FuncionarioRepository {

    @Override
    public Funcionario buscaPorId(Long id) {

        FuncionarioEntity entity = FuncionarioEntity.findById(id);

        if (entity == null)
            return null;

        return entity.toDomain();
    }

    @Override
    public List<Funcionario> buscaPorEmpresa(Long empresaId) {

        List<FuncionarioEntity> entities = FuncionarioEntity
                .list("empresaId = ?1 and active = true", empresaId);

        return entities.stream()
                .map(FuncionarioEntity::toDomain)
                .toList();
    }

    @Override
    public void cadastra(Funcionario funcionario) {
        FuncionarioEntity entity = FuncionarioEntity.fromDomain(funcionario);

        entity.persist();

    }

    @Override
    public void atualizar(Funcionario funcionario) {
        FuncionarioEntity entity = FuncionarioEntity.findById(funcionario.getId());

        if (entity != null) {
            entity.name = funcionario.getName();
            entity.telefone = funcionario.getTelefone().getPhone();
            entity.active = funcionario.getActive();
            entity.empresaId = funcionario.getEmpresaId();

        }
    }
}
