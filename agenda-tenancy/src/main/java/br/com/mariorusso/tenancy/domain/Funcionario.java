package br.com.mariorusso.tenancy.domain;

import br.com.mariorusso.tenancy.domain.valueobject.Telefone;


public class Funcionario {
    private Long id;
    private String name;
    private Telefone telefone;
    private boolean active;

    private Long empresaId;

    public Funcionario() {   }

    public Funcionario(Long id, String nome, String phone, Long empresa, Boolean active) {

        this.id = id;
        this.name = nome.trim();
        this.telefone = new Telefone(phone);
        this.active = active;
        this.empresaId = empresa;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public boolean getActive() {
        return active;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    private void validaFuncionario(Long serviceId, String nome) {
        if (serviceId == null || serviceId <= 0)
            throw new IllegalArgumentException("Serviço inválido");

        if (nome.length() < 3 || nome.isBlank())
            throw new IllegalArgumentException("Nome para Funcionário inválido");
    }

    public void alterarTelefone(String telefone) {
        this.telefone = new Telefone(telefone);

    }

    public void alterarNome(String nome) {
        if (nome == null || nome.trim().length() < 3)
            throw new IllegalArgumentException("Nome para Funcionário inválido");
        this.name = nome.trim();
    }

    public void desativar() {
        this.active = false;
    }

    public void ativar() {
        this.active = true;

    }

    public boolean estaDisponivel() {
        return this.active;
    }

    public void associarEmpresa(Long empresaId) {
        if (empresaId == null || empresaId <= 0)
            throw new IllegalArgumentException("empresa inválido, empresa não pode ser associado ao funcionario");
        this.empresaId = empresaId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Funcionario that = (Funcionario) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
