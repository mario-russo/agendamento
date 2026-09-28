package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.ports.in.BuscaFuncionarioUseCase;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.Pagina;



public class BuscaFuncionarioImpl implements BuscaFuncionarioUseCase {

    private final FuncionarioRepository repository;

    public BuscaFuncionarioImpl(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pagina<Funcionario> buscaPorEmpresa(Long empresaId, int pagina, int tamanho) {

        Pagina<Funcionario> funcionarios = repository.buscaPorEmpresaPorPagina(empresaId, pagina,tamanho);

        return funcionarios;
    }
}
