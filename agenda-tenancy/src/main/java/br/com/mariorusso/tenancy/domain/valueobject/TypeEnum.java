package br.com.mariorusso.tenancy.domain.valueobject;

public enum TypeEnum {

    EMPRESA("empresa"),
    FUNCIONARIO("funcionario"),
    COMUN("comun");

    private final String descricao;

    TypeEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescicao() {
        return descricao;
    }
}
