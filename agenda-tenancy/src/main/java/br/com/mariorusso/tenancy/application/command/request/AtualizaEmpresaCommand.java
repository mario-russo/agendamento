package br.com.mariorusso.tenancy.application.command.request;



public record AtualizaEmpresaCommand(

        String name,
        String cnpj,
        String telefone,
        String email,
        String endereco,
        String municipio,
        String estado
) { }
