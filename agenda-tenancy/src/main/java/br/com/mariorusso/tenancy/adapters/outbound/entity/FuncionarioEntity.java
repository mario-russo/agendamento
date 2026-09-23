package br.com.mariorusso.tenancy.adapters.outbound.entity;

import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.valueobject.Telefone;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "funcionario")
public class FuncionarioEntity extends PanacheEntity {


    public Long id;
    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String telefone;

    public boolean active;

    @Column(name = "empresa_id")
    public Long empresaId;

    public FuncionarioEntity() {
    }

    public static FuncionarioEntity fromDomain(Funcionario funcionario) {
        if (funcionario == null)
            return null;
        FuncionarioEntity entity = new FuncionarioEntity();
        entity.active = funcionario.getActive();
        entity.empresaId = funcionario.getEmpresaId();
        entity.name = funcionario.getName();
        entity.telefone = funcionario.getTelefone().getPhone();
        return entity;
    }

    public Funcionario toDomain() {
       return new Funcionario(
                this.id,
                this.name,
                this.telefone,
                this.empresaId,
                this.active
        );
    }

}
