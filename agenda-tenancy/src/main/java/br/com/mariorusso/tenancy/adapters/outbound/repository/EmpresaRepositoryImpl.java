package br.com.mariorusso.tenancy.adapters.outbound.repository;


import br.com.mariorusso.tenancy.adapters.outbound.entity.EmpresaEntity;
import br.com.mariorusso.tenancy.application.command.request.AtualizaEmpresaCommand;
import br.com.mariorusso.tenancy.application.command.request.EmpresaRequest;
import br.com.mariorusso.tenancy.application.ports.out.EmpresaRepository;
import br.com.mariorusso.tenancy.domain.Empresa;
import br.com.mariorusso.tenancy.domain.valueobject.Email;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class EmpresaRepositoryImpl implements EmpresaRepository {


    @Override
    public Optional<Empresa> buscaPorId(Long id) {
        return EmpresaEntity.<EmpresaEntity>findByIdOptional(id)
                .map(EmpresaEntity::toDomain);
    }

    @Override
    public void atualizar(Empresa empresa) {

        EmpresaEntity entity = EmpresaEntity.findById(empresa.getId());

        entity.name = empresa.getName();
        entity.cnpj = empresa.getCnpj().getValue();
        entity.telefone = empresa.getTelefone().getPhone();
        entity.email = empresa.getEmail().getValue();
        entity.enderecoEstado = empresa.getEndereco().getEstado();
        entity.enderecoLogradouro = empresa.getEndereco().getEndereco();
        entity.enderecoMunicipio = empresa.getEndereco().getMunicipio();
        PanacheEntityBase.getEntityManager().merge(entity);


    }
}
