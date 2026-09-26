package br.com.mariorusso.tenancy.application.usecase;


import br.com.mariorusso.tenancy.application.dtos.request.FuncionarioRequestDto;
import br.com.mariorusso.tenancy.application.ports.in.SalvaFuncionarioUseCase;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;

public class SalvaFuncionarioIpml implements SalvaFuncionarioUseCase {

    private final FuncionarioRepository funcionarioRepository;


    public SalvaFuncionarioIpml(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public void execute(FuncionarioRequestDto dto) {

        Funcionario funcionario = new Funcionario(
                null,
                dto.nome(),
                dto.telefone(),
                dto.empresaId(),
                true
        );

        funcionarioRepository.cadastra(funcionario);

    }
}
