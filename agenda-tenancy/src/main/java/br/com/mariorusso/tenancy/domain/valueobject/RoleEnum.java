package br.com.mariorusso.tenancy.domain.valueobject;

public enum RoleEnum {

    ESCRITA("escrita"),
    LEITURA("leitura");

    private final String descricao;

    RoleEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescicao() {
        return descricao;
    }
}
