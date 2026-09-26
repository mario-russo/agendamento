package br.com.mariorusso.tenancy.application.ports.in;

public interface DesativarFuncionarioUsecase {
    void desativar(Long id, Long empresaId);
}
