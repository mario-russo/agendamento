package br.com.mariorusso.tenancy.adapters.outbound.repository;


import br.com.mariorusso.tenancy.adapters.outbound.entity.FuncionarioEntity;
import br.com.mariorusso.tenancy.application.ports.ou.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FuncionarioRepositoryImpl implements FuncionarioRepository {

//    private FuncionarioEntity funcionarioEtity;


    @Override
    public Funcionario buscaPorId(Long id) {

        FuncionarioEntity entity = FuncionarioEntity.find("id", id).firstResult();

        return entity.toDomain();
    }

    @Override
    public List<Funcionario> buscaPorEmpresa(Long empresaId) {

        PanacheQuery<FuncionarioEntity> entity = FuncionarioEntity.find("empresaId", empresaId);
        List<Funcionario> funcionario = entity.stream().map(e -> e.toDomain()).toList();

        return funcionario;
    }

    @Override
    public void cadastra(Funcionario funcionario) {

    }
}
