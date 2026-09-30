package br.com.mariorusso.tenancy.application.command.request;

public record SalvaFuncionarioCommand(
         String nome,
         String telefone,
         Long empresaId) {
    public SalvaFuncionarioCommand comEmpresaId(Long empresaId) {
        return new SalvaFuncionarioCommand(nome, telefone, empresaId);
    }
}
