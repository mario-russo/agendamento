package br.com.mariorusso.tenancy.application.ports.in;

import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.Pagina;

import java.util.List;

public interface BuscaFuncionarioUseCase {
    Pagina<Funcionario> buscaPorEmpresa(Long empresaId, int pagina, int tamanho);
}
