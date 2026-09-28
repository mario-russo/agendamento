package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.dtos.request.SalvaFuncionarioCommand;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SalvaFuncionarioImplTest {
    // Constantes centralizadas para facilitar a manutenção das regras do domínio (ex: telefone válido)
    private static final String NOME_FUNCIONARIO = "Mário Russo";
    private static final String TELEFONE_FUNCIONARIO = "21988888888";
    private static final Long EMPRESA_ID = 10L;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    private SalvaFuncionarioImpl salvaFuncionario;

    @BeforeEach
    void setUp() {
        salvaFuncionario = new SalvaFuncionarioImpl(funcionarioRepository);
    }

    @Test
    @DisplayName("Deve instanciar um funcionário do domínio e cadastrá-lo com sucesso através do repositório")
    void deveSalvarFuncionarioComSucesso() {
        SalvaFuncionarioCommand command = new SalvaFuncionarioCommand(
                NOME_FUNCIONARIO,
                TELEFONE_FUNCIONARIO,
                EMPRESA_ID
        );

        ArgumentCaptor<Funcionario> funcionarioCaptor = ArgumentCaptor.forClass(Funcionario.class);

        salvaFuncionario.execute(command);

        verify(funcionarioRepository, times(1)).cadastrar(funcionarioCaptor.capture());

        Funcionario funcionarioCadastrado = funcionarioCaptor.getValue();
        assertEquals(NOME_FUNCIONARIO, funcionarioCadastrado.getName());
        assertEquals(TELEFONE_FUNCIONARIO, funcionarioCadastrado.getTelefone().getPhone());
        assertEquals(EMPRESA_ID, funcionarioCadastrado.getEmpresaId());
    }
}