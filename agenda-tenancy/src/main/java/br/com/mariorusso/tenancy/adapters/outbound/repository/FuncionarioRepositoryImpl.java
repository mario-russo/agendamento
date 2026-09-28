package br.com.mariorusso.tenancy.adapters.outbound.repository;


import br.com.mariorusso.tenancy.adapters.outbound.entity.FuncionarioEntity;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.Pagina;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioNotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.find;

@ApplicationScoped
public class FuncionarioRepositoryImpl implements FuncionarioRepository {

    @Override
    public Optional<Funcionario> buscaPorId(Long id) {

        return FuncionarioEntity.<FuncionarioEntity>findByIdOptional(id)
                .map(FuncionarioEntity::toDomain);
    }

    @Override
    public void cadastrar(Funcionario funcionario) {
        FuncionarioEntity entity = FuncionarioEntity.fromDomain(funcionario);

        entity.persist();

    }

    @Override
    public void atualizar(Funcionario funcionario) {
        FuncionarioEntity entity = FuncionarioEntity.<FuncionarioEntity>findByIdOptional(funcionario.getId())
                .orElseThrow(() -> new FuncionarioNotFoundException("Funcionário não encontrado"));

        entity.name = funcionario.getName();
        entity.telefone = funcionario.getTelefone().getPhone();
        entity.active = funcionario.getActive();
        entity.empresaId = funcionario.getEmpresaId();
        PanacheEntityBase.getEntityManager().merge(entity);

    }

    @Override
    public Pagina<Funcionario> buscaPorEmpresaPorPagina(Long empresaId, int pagina, int tamanho) {
        PanacheQuery<FuncionarioEntity> query = FuncionarioEntity.find("empresaId", empresaId);

        query.page(Page.of(pagina, tamanho));

        List<FuncionarioEntity> entidades = query.list();

        long totalElementos = query.count();

        List<Funcionario> funcionariosDoDominio = entidades.stream()
                .map(FuncionarioEntity::toDomain)
                .toList();

        return new Pagina<>(funcionariosDoDominio, pagina, tamanho, totalElementos);
    }
}
