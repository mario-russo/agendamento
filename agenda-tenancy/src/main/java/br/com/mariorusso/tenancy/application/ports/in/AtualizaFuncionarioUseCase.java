package br.com.mariorusso.tenancy.application.ports.in;

public interface AtualizaFuncionarioUseCase {
    void atualizar( Long id, Long empresaId, String nome, String telefone);
}
