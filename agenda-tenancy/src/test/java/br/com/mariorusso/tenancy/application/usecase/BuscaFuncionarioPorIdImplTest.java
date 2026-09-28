package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscaFuncionarioPorIdImplTest {
    @Mock
    private FuncionarioRepository funcionarioRepository;

    private BuscaFuncionarioPorIdImpl buscaFuncionarioPorId;

    @BeforeEach
    void setUp() {
        buscaFuncionarioPorId = new BuscaFuncionarioPorIdImpl(funcionarioRepository);
    }

    @Nested
    @DisplayName("Cenários de Sucesso")
    class CenariosSucesso {

        @Test
        @DisplayName("Deve retornar o funcionário quando ele existir e pertencer à empresa informada")
        void deveRetornarFuncionarioComSucesso() {
            // Arrange
            Long id = 1L;
            Long empresaId = 10L;
            Funcionario funcionario = new Funcionario("Mário Russo", "21988888888", empresaId);

            when(funcionarioRepository.buscaPorId(id)).thenReturn(Optional.of(funcionario));

            Optional<Funcionario> resultado = buscaFuncionarioPorId.busca(id, empresaId);

            assertTrue(resultado.isPresent(), "O funcionário deveria ser retornado");
            assertEquals("Mário Russo", resultado.get().getName());
            assertEquals(empresaId, resultado.get().getEmpresaId());

            verify(funcionarioRepository, times(1)).buscaPorId(id);
        }

        @Test
        @DisplayName("Deve retornar um Optional vazio se o funcionário não existir no repositório")
        void deveRetornarVazioQuandoFuncionarioNaoExistir() {
            Long id = 1L;
            Long empresaId = 10L;

            when(funcionarioRepository.buscaPorId(id)).thenReturn(Optional.empty());

            Optional<Funcionario> resultado = buscaFuncionarioPorId.busca(id, empresaId);

            assertTrue(resultado.isEmpty(), "O resultado deveria ser um Optional vazio");
            verify(funcionarioRepository, times(1)).buscaPorId(id);
        }
    }

    @Nested
    @DisplayName("Cenários de Validação de Segurança")
    class CenariosSeguranca {

        @Test
        @DisplayName("Deve retornar um Optional vazio se o funcionário existir mas pertencer a outra empresa")
        void deveRetornarVazioQuandoFuncionarioForDeOutraEmpresa() {
            Long id = 1L;
            Long empresaIdRequisicao = 10L;
            Long empresaIdDiferente = 99L; // Empresa diferente cadastrada no banco

            Funcionario funcionarioDeOutraEmpresa = new Funcionario("Mário Russo", "21988888888", empresaIdDiferente);

            when(funcionarioRepository.buscaPorId(id)).thenReturn(Optional.of(funcionarioDeOutraEmpresa));

            Optional<Funcionario> resultado = buscaFuncionarioPorId.busca(id, empresaIdRequisicao);

            assertTrue(resultado.isEmpty(), "O filtro deveria bloquear e retornar um Optional vazio");
            verify(funcionarioRepository, times(1)).buscaPorId(id);
        }
    }

}