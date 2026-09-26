package br.com.mariorusso.tenancy.adapters.outbound.entity;


import br.com.mariorusso.share.Endereco;
import br.com.mariorusso.tenancy.domain.Empresa;
import br.com.mariorusso.tenancy.domain.valueobject.Cnpj;
import br.com.mariorusso.tenancy.domain.valueobject.Email;
import br.com.mariorusso.tenancy.domain.valueobject.Telefone;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "empresa")
public class EmpresaEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    public String name;

    @Column(unique = true)
    public String cnpj;

    @Column
    public String telefone;

    @Column
    public String email;

    @Column
    public String endereco;

    @Column(name = "usuario_id")
    public Long usuarioId;

    @Column(name = "funcionario_id")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "empresa_funcionarios",
            joinColumns = @JoinColumn(name = "empresa_id")
    )
    public Set<Long> funcionarioId;

    @Column(name = "endereco_logradouro")
    public String enderecoLogradouro; // Mapeia o atributo 'endereco' do domínio

    @Column(name = "endereco_municipio")
    public String enderecoMunicipio;

    @Column(name = "endereco_estado")
    public String enderecoEstado;

    @Column
    public boolean active;

    @Column(name = "date_create")
    public LocalDateTime dateCreate;

    @Column(name = "date_update")
    public LocalDateTime dateUpdate;

    public EmpresaEntity() {
    }

    public static EmpresaEntity fromDomain(Empresa empresa) {
        if (empresa == null) {
            return null;
        }

        EmpresaEntity entity = new EmpresaEntity();

        entity.name = empresa.getName();
        entity.cnpj = empresa.getCnpj() != null ? empresa.getCnpj().getValue() : null;
        entity.email = empresa.getEmail() != null ? empresa.getEmail().getValue() : null;
        entity.telefone = empresa.getTelefone() != null ? empresa.getTelefone().getPhone() : null;

        if (empresa.getEndereco() != null) {
            entity.enderecoLogradouro = empresa.getEndereco().getEndereco();
            entity.enderecoMunicipio = empresa.getEndereco().getMunicipio();
            entity.enderecoEstado = empresa.getEndereco().getEstado();
        }

        entity.usuarioId = empresa.getUsuarioId();

        entity.funcionarioId = empresa.getFuncionarios() != null ? new java.util.HashSet<>(empresa.getFuncionarios()) : new java.util.HashSet<>();

        entity.active = empresa.isActive();
        entity.dateCreate = empresa.getDateCreate();
        entity.dateUpdate = empresa.getDateUpdate();

        return entity;
    }

    public Empresa toDomain() {
        return Empresa.reidratar(
                this.id,
                this.name,
                this.cnpj != null ? new Cnpj(this.cnpj) : null,
                this.telefone != null ? new Telefone(this.telefone) : null,
                this.email != null ? new Email(this.email) : null,
                new Endereco(this.enderecoLogradouro, this.enderecoMunicipio, this.enderecoEstado),
                this.usuarioId,
                this.funcionarioId,
                this.active,
                this.dateCreate,
                this.dateUpdate
        );

    }

}
