package br.com.mariorusso.tenancy.application.usecase;


import br.com.mariorusso.tenancy.application.dtos.request.SalvaFuncionarioCommand;
import br.com.mariorusso.tenancy.application.ports.in.SalvaFuncionarioUseCase;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import jakarta.transaction.Transactional;

public class SalvaFuncionarioImpl implements SalvaFuncionarioUseCase {

    private final FuncionarioRepository funcionarioRepository;


    public SalvaFuncionarioImpl(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    @Transactional
    public void execute(SalvaFuncionarioCommand dto) {

        Funcionario funcionario = new Funcionario(
                dto.nome(),
                dto.telefone(),
                dto.empresaId()
        );

        funcionarioRepository.cadastrar(funcionario);

    }
}
