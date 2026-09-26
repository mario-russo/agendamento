package br.com.mariorusso.tenancy.application.dtos.request;

public record FuncionarioRequestDto(
         String nome,
         String telefone,
         Long empresaId) {
    public FuncionarioRequestDto comEmpresaId(Long empresaId) {
        return new FuncionarioRequestDto(nome, telefone, empresaId);
    }
}
