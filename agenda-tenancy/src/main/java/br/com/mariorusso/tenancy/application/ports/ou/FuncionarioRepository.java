package br.com.mariorusso.tenancy.application.ports.ou;

import br.com.mariorusso.tenancy.domain.Funcionario;

import java.util.List;

public interface FuncionarioRepository {
    Funcionario buscaPorId(Long id);
    List<Funcionario> buscaPorEmpresa(Long empresaId);
    void  cadastra(Funcionario funcionario);
}
