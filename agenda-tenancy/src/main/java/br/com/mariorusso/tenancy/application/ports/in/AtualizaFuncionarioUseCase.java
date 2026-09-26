package br.com.mariorusso.tenancy.application.ports.in;

public interface AtualizaFuncionarioUseCase {
    void atualizar(String nome, String telefone, Long id, Long empresaId);
}
