package br.com.mariorusso.tenancy.application.command.request;

public record AtualizaFuncionarioDto(
        String nome,
        String telefone,
        Long id) {
}
