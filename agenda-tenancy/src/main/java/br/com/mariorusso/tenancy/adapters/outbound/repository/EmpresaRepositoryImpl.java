package br.com.mariorusso.tenancy.adapters.outbound.repository;


import br.com.mariorusso.tenancy.adapters.outbound.entity.EmpresaEntity;
import br.com.mariorusso.tenancy.application.dtos.request.EmpresaRequest;
import br.com.mariorusso.tenancy.application.ports.out.EmpresaRepository;
import br.com.mariorusso.tenancy.domain.Empresa;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class EmpresaRepositoryImpl implements EmpresaRepository {


    @Override
    @Transactional
    public void cadastra(EmpresaRequest request) {

        Empresa empresa = request.toDomain();

        EmpresaEntity entity = EmpresaEntity.fromDomain(empresa);

        entity.persist();


    }

    @Override
    public Empresa buscaPorId(Long id) {
        EmpresaEntity entity = EmpresaEntity.find("usuarioId", id).firstResult();
        return  entity.toDomain();
    }


}
