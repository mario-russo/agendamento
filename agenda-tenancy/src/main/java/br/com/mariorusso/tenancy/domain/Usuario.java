package br.com.mariorusso.tenancy.domain;

import br.com.mariorusso.tenancy.domain.valueobject.Email;
import br.com.mariorusso.tenancy.domain.valueobject.RoleEnum;
import br.com.mariorusso.tenancy.domain.valueobject.TypeEnum;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Usuario {

    private Long id;
    private Email email;
    private String password;
    private String name;
    private Set<RoleEnum> role = new HashSet<>();
    private LocalDateTime dateCreate;
    private boolean active;
    private TypeEnum tipo;

    public Usuario() {
    }

    public Usuario(Long id, String email, String password, String name, Set<RoleEnum> role, TypeEnum type) {
        valida(name, role, password);
        this.email = new Email(email);
        this.password = password;
        this.name = name;
        this.dateCreate = LocalDateTime.now();
        this.active = true;
        this.role = role;
        this.tipo = type;
        this.id = id;
    }

    public Usuario(String email, String password, String name, Set<RoleEnum> role, TypeEnum type) {
        this(null, email, password, name, role, type);
    }

    private void valida(String nome, Set<RoleEnum> role, String password) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("nome não valido ");

        if (role == null)
            throw new IllegalArgumentException("role não valido ");

        if (password == null || password.isBlank())
            throw new IllegalArgumentException("password não valido ");


    }

    public void desativar() {
        this.active = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Email getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Set<RoleEnum> getRole() {
        return Collections.unmodifiableSet(role);
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public boolean isActive() {
        return active;
    }

    public TypeEnum getTipo() {
        return tipo;
    }

}
