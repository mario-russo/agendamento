package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioDeOutraEmpresaException;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesativarFuncionarioImplTest {


    private static final String NOME_FUNCIONARIO = "Mário Russo";
    private static final String TELEFONE_FUNCIONARIO = "21988888888";

    @Mock
    private FuncionarioRepository funcionarioRepository;

    private DesativarFuncionarioImpl desativarFuncionario;

    @BeforeEach
    void setUp() {
        desativarFuncionario = new DesativarFuncionarioImpl(funcionarioRepository);
    }

    @Nested
    @DisplayName("Cenários de Sucesso")
    class CenariosSucesso {

        @Test
        @DisplayName("Deve desativar o funcionário com sucesso quando pertencer à empresa correta")
        void deveDesativarFuncionarioComSucesso() {
            Long id = 1L;
            Long empresaId = 10L;

            Funcionario funcionarioMock = spy(new Funcionario(NOME_FUNCIONARIO, TELEFONE_FUNCIONARIO, empresaId));
            when(funcionarioRepository.buscaPorId(id)).thenReturn(Optional.of(funcionarioMock));


            desativarFuncionario.desativar(id, empresaId);

            verify(funcionarioMock, times(1)).desativar();
            verify(funcionarioRepository, times(1)).atualizar(funcionarioMock);
        }
    }

    @Nested
    @DisplayName("Cenários de Falha (Exceções)")
    class CenariosFalha {

        @Test
        @DisplayName("Deve lançar exceção quando o funcionário não for encontrado")
        void deveLancarExcecaoQuandoFuncionarioNaoEncontrado() {
            Long id = 1L;
            Long empresaId = 10L;
            when(funcionarioRepository.buscaPorId(id)).thenReturn(Optional.empty());

            FuncionarioNotFoundException ex = assertThrows(FuncionarioNotFoundException.class, () ->
                    desativarFuncionario.desativar(id, empresaId)
            );
            assertEquals("Funcionário não encontrado", ex.getMessage());

            verify(funcionarioRepository, never()).atualizar(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando o funcionário pertencer a uma empresa diferente")
        void deveLancarExcecaoQuandoFuncionarioPertenceAOutraEmpresa() {

            Long id = 1L;
            Long empresaIdRequisicao = 10L;
            Long empresaIdDiferente = 99L;

            Funcionario funcionarioMock = new Funcionario(NOME_FUNCIONARIO, TELEFONE_FUNCIONARIO, empresaIdDiferente);
            when(funcionarioRepository.buscaPorId(id)).thenReturn(Optional.of(funcionarioMock));

            FuncionarioDeOutraEmpresaException ex = assertThrows(FuncionarioDeOutraEmpresaException.class, () ->
                    desativarFuncionario.desativar(id, empresaIdRequisicao)
            );
            assertEquals("Funcionário não pertence a empresa", ex.getMessage());

            verify(funcionarioRepository, never()).atualizar(any());
        }
    }
}