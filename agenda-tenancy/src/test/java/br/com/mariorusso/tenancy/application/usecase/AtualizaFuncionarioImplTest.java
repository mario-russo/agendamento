package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioDeOutraEmpresaException;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AtualizaFuncionarioImplTest {
    @Mock
    private FuncionarioRepository funcionarioRepository;

    private AtualizaFuncionarioImpl atualizaFuncionario;

    private static final String TELEFONE_VALIDO = "11123456789";

    @BeforeEach
    void setUp() {
        atualizaFuncionario = new AtualizaFuncionarioImpl(funcionarioRepository);
    }

    @Test
    @DisplayName("Deve atualizar o nome e telefone do funcionário com sucesso")
    void deveAtualizarFuncionarioComSucesso() {

        Long id = 1L;
        Long empresaId = 10L;
        String novoNome = "Mário Russo Alterado";
        String novoTelefone = "21123456789";

        Funcionario funcionarioMock = spy(new Funcionario("Nome Antigo", TELEFONE_VALIDO, empresaId));

        when(funcionarioRepository.buscaPorId(id)).thenReturn(Optional.of(funcionarioMock));

        atualizaFuncionario.atualizar(id, empresaId, novoNome, novoTelefone);

        assertEquals(novoNome, funcionarioMock.getName());
        assertEquals(novoTelefone, funcionarioMock.getTelefone().getPhone());

        verify(funcionarioMock).alterarNome(novoNome);
        verify(funcionarioMock).alterarTelefone(novoTelefone);
        verify(funcionarioRepository).atualizar(funcionarioMock);


    }

    @Test
    @DisplayName("Deve lançar exceção quando o funcionário não for encontrado")
    void deveLancarExcecaoQuandoFuncionarioNaoEncontrado() {

        Long id = 1L;
        Long empresaId = 10L;
        when(funcionarioRepository.buscaPorId(id)).thenReturn(Optional.empty());

        FuncionarioNotFoundException ex = assertThrows(FuncionarioNotFoundException.class, () ->
                atualizaFuncionario.atualizar(id, empresaId, "Nome", "Telefone")
        );

        assertEquals("Funcionário não encontrado", ex.getMessage());

        // Garante que o método de atualizar nunca foi chamado
        verify(funcionarioRepository, never()).atualizar(any());

    }

    @Test
    @DisplayName("Deve lançar exceção quando o funcionário pertencer a outra empresa")
    void deveLancarExcecaoQuandoFuncionarioPertenceAOutraEmpresa() {
        Long id = 1L;
        Long empresaIdRequisicao = 10L;
        Long empresaIdDiferente = 99L; // Empresa diferente

        Funcionario funcionarioMock = new Funcionario("Nome", TELEFONE_VALIDO, empresaIdDiferente);
        when(funcionarioRepository.buscaPorId(id)).thenReturn(Optional.of(funcionarioMock));

        FuncionarioDeOutraEmpresaException ex = assertThrows(FuncionarioDeOutraEmpresaException.class, () ->
                atualizaFuncionario.atualizar(id, empresaIdRequisicao, "Nome", "Telefone")
        );

        assertEquals("Funcionário não pertence à empresa", ex.getMessage());
        verify(funcionarioRepository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Não deve alterar propriedades se os parâmetros forem nulos")
    void naoDeveAlterarPropriedadesSeParametrosForemNulos() {

        Long id = 1L;
        Long empresaId = 10L;

        Funcionario funcionarioMock = spy(new Funcionario( "Nome Original", TELEFONE_VALIDO, empresaId));
        when(funcionarioRepository.buscaPorId(id)).thenReturn(Optional.of(funcionarioMock));

        // Act
        atualizaFuncionario.atualizar(id, empresaId, null, null);

        // Assert
        verify(funcionarioMock, never()).alterarNome(anyString());
        verify(funcionarioMock, never()).alterarTelefone(anyString());
        verify(funcionarioRepository).atualizar(funcionarioMock);
    }

    @Test
    @DisplayName("Deve alterar apenas o nome quando telefone for nulo")
    void deveAlterarApenasNome() {
        Long id = 1L;
        Long empresaId = 10L;
        Funcionario spy = spy(new Funcionario("Nome Antigo", TELEFONE_VALIDO, empresaId));
        when(funcionarioRepository.buscaPorId(id)).thenReturn(Optional.of(spy));

        atualizaFuncionario.atualizar(id, empresaId, "Novo Nome", null);

        assertEquals("Novo Nome", spy.getName());
        assertEquals("11123456789", spy.getTelefone().getPhone());
        verify(spy).alterarNome("Novo Nome");
        verify(spy, never()).alterarTelefone(anyString());
    }

    @Test
    @DisplayName("Deve alterar apenas o telefone quando nome for nulo")
    void deveAlterarApenasTelefone() {
        Long id = 1L;
        Long empresaId = 10L;
        Funcionario spy = spy(new Funcionario("Nome Original",TELEFONE_VALIDO, empresaId));
        when(funcionarioRepository.buscaPorId(id)).thenReturn(Optional.of(spy));

        atualizaFuncionario.atualizar(id, empresaId, null, "21999999999");

        assertEquals("Nome Original", spy.getName());
        assertEquals("21999999999", spy.getTelefone().getPhone());
        verify(spy).alterarTelefone("21999999999");
        verify(spy, never()).alterarNome(anyString());
    }

}