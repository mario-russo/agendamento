package br.com.mariorusso.tenancy.domain;

import br.com.mariorusso.share.Endereco;
import br.com.mariorusso.tenancy.domain.valueobject.Cnpj;
import br.com.mariorusso.tenancy.domain.valueobject.Email;
import br.com.mariorusso.tenancy.domain.valueobject.Telefone;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Empresa {
    private Long id;
    private String name;
    private Cnpj cnpj;
    private Telefone telefone;
    private Email email;
    private Endereco endereco;
    private Long usuarioId;
    private Set<Long> funcionarioId;

    private boolean active;
    private LocalDateTime dateCreate;
    private LocalDateTime dateUpdate;


    private Empresa() {
    }

    public Empresa(String name, String cnpj, String email, String telefone, Endereco endereco, Long usuario) {
        validaEmpresa(name);
        inicia();
        this.name = name.trim();
        this.cnpj = new Cnpj(cnpj);
        this.email = new Email(email);
        this.telefone = new Telefone(telefone);
        this.funcionarioId = new HashSet<>();
        this.endereco = endereco;
        this.usuarioId = usuario;
    }

    public static Empresa reidratar(
            Long id,
            String name,
            Cnpj cnpj,
            Telefone telefone,
            Email email,
            Endereco endereco,
            Long usuarioId,
            Set<Long> funcionarios,
            boolean active,
            LocalDateTime dateCreate,
            LocalDateTime dateUpdate
    ) {
        Empresa empresa = new Empresa();
        empresa.id = id;
        empresa.name = name;
        empresa.cnpj = cnpj;
        empresa.telefone = telefone;
        empresa.email = email;
        empresa.endereco = endereco;
        empresa.usuarioId = usuarioId;
        empresa.funcionarioId = funcionarios != null ? new HashSet<>(funcionarios) : new HashSet<>();
        empresa.active = active;
        empresa.dateCreate = dateCreate;
        empresa.dateUpdate = dateUpdate;

        return empresa;
    }

    private void inicia() {
        this.active = true;
        this.dateCreate = LocalDateTime.now();
        this.dateUpdate = LocalDateTime.now();
    }

    private void validaEmpresa(String nome) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome invalido");
    }

    public void adicionaFuncionario(Long funcionarioId) {
        boolean existeFuncionario = this.funcionarioId.contains(funcionarioId);
        if (existeFuncionario)
            throw new IllegalArgumentException("Funcionario já está na empresa");

        this.funcionarioId.add(funcionarioId);
    }

    public void adicionaFuncionarios(Set<Long> novosFuncionarios) {
        if (novosFuncionarios != null) {
            this.funcionarioId.addAll(novosFuncionarios);
        }
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Cnpj getCnpj() {
        return cnpj;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public Email getEmail() {
        return email;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public LocalDateTime getDateUpdate() {
        return dateUpdate;
    }

    public Set<Long> getFuncionarios() {
        return funcionarioId;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }
}
