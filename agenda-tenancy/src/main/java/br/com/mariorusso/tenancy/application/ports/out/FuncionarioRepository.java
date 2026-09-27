package br.com.mariorusso.tenancy.application.ports.out;

import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.Pagina;

import java.util.Optional;

public interface FuncionarioRepository {
    Optional<Funcionario> buscaPorId(Long id);
    void  cadastrar(Funcionario funcionario);
    void atualizar(Funcionario funcionario);
    Pagina<Funcionario> buscaPorEmpresaPorPagina(Long empresaId, int pagina, int tamanho);
}
