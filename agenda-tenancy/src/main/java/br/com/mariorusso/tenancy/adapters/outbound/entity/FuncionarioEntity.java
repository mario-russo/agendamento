package br.com.mariorusso.tenancy.adapters.outbound.entity;

import br.com.mariorusso.tenancy.domain.Funcionario;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;


@Entity
@Table(name = "funcionario")
public class FuncionarioEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        FuncionarioEntity entity = (FuncionarioEntity) o;
        return id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
