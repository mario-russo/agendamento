package br.com.mariorusso.tenancy.adapters.outbound.entity;

import br.com.mariorusso.tenancy.domain.Usuario;
import br.com.mariorusso.tenancy.domain.valueobject.RoleEnum;
import br.com.mariorusso.tenancy.domain.valueobject.TypeEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.find;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "email"))
    })
    public EmailEntity email;

    @Column(nullable = false)
    public String password;

    @Column(nullable = false)
    public String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Enumerated(EnumType.STRING)
    public Set<RoleEnum> role = new HashSet<>();

    @Column(name = "data_create")
    public LocalDateTime dateCreate;

    @Column(nullable = false)
    public boolean active;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public TypeEnum tipo;

    public UsuarioEntity() {

    }

    public static UsuarioEntity findByEmail(String emailStr) {

        return find("email.value", emailStr).firstResult();
    }

    public Usuario toDomain() {
        return new Usuario(id, email.value, password, name, role, tipo);

    }

    public static UsuarioEntity fromDomain(Usuario usuario) {
        if (usuario == null)
            return null;
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.name = usuario.getName();
        usuarioEntity.email = new EmailEntity(usuario.getEmail().getValue());
        usuarioEntity.password = usuario.getPassword();
        usuarioEntity.role = usuario.getRole();
        usuarioEntity.active = usuario.isActive();
        usuarioEntity.tipo = usuario.getTipo();
        usuarioEntity.dateCreate = usuario.getDateCreate();
        return usuarioEntity;
    }

}
