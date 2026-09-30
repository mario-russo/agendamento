package br.com.mariorusso.tenancy.application.command.request;


import jakarta.validation.constraints.NotBlank;

public record AtualizaEmpresaCommand(

        Long id,
        Long usuarioId,
        @NotBlank(message = "O nome não pode ser vazio.")
        String name,

        @NotBlank(message = "O CNPJ não pode ser vazio.")
        String cnpj,

        @NotBlank(message = "O telefone não pode ser vazio.")
        String telefone,

        @NotBlank(message = "O e-mail não pode ser vazio.")
        String email,

        @NotBlank(message = "O endereço não pode ser vazio.")
        String endereco,

        @NotBlank(message = "O município não pode ser vazio.")
        String municipio,

        @NotBlank(message = "O estado não pode ser vazio.")
        String estado
) {


    public static AtualizaEmpresaCommand comId(Long id, AtualizaEmpresaCommand request) {
        return new AtualizaEmpresaCommand(
                id,
                request.usuarioId,
                request.name().trim(),
                request.cnpj().trim(),
                request.telefone().trim(),
                request.email().trim(),
                request.endereco().trim(),
                request.municipio().trim(),
                request.estado().trim()
        );
    }
}
