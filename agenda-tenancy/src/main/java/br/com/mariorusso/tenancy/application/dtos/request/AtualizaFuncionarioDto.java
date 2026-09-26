package br.com.mariorusso.tenancy.application.dtos.request;

public record AtualizaFuncionarioDto(
        String nome,
        String telefone,
        Long id) {
}
