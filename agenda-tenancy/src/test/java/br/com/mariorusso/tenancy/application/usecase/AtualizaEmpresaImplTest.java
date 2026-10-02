package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.share.Endereco;
import br.com.mariorusso.tenancy.application.command.request.AtualizaEmpresaCommand;
import br.com.mariorusso.tenancy.application.ports.out.EmpresaRepository;
import br.com.mariorusso.tenancy.domain.Empresa;
import br.com.mariorusso.tenancy.domain.exception.EmpresaNotFound;
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
class AtualizaEmpresaImplTest {

    @Mock
    private EmpresaRepository empresaRepository;

    private AtualizaEmpresaImpl atualizaEmpresa;

    // Constantes válidas
    private static final String NOME_VALIDO = "Mário Russo LTDA";
    private static final String CNPJ_VALIDO = "33592510000154";
    private static final String EMAIL_VALIDO = "contato@mariorusso.com";
    private static final String TELEFONE_VALIDO = "21988888888";
    private static final Long EMPRESA_ID = 1L;
    private static final Long USUARIO_ID = 1L;

    @BeforeEach
    void setUp() {
        atualizaEmpresa = new AtualizaEmpresaImpl(empresaRepository);
    }

    private Empresa criarEmpresa() {
        return new Empresa(
                NOME_VALIDO,
                CNPJ_VALIDO,
                EMAIL_VALIDO,
                TELEFONE_VALIDO,
                new Endereco("Rua A", "Rio de Janeiro", "RJ"),
                USUARIO_ID
        );
    }

    // ------------------------------------------------------------
    // Cenários de Sucesso
    // ------------------------------------------------------------

    @Nested
    @DisplayName("Cenários de Sucesso")
    class CenariosSucesso {

        @Test
        @DisplayName("Deve atualizar todos os campos quando todos forem enviados")
        void deveAtualizarTodosOsCampos() {
            Empresa empresa = spy(criarEmpresa());
            when(empresaRepository.buscaPorId(EMPRESA_ID)).thenReturn(Optional.of(empresa));

            AtualizaEmpresaCommand command = new AtualizaEmpresaCommand(
                    "Novo Nome",
                    "11222333000181",
                    "11977777777",
                    "novo@email.com",
                    "Rua Nova",
                    "São Paulo",
                    "SP"
            );

            atualizaEmpresa.atualizar(EMPRESA_ID, command);

            assertEquals("Novo Nome", empresa.getName());
            assertEquals("11222333000181", empresa.getCnpj().getValue());
            assertEquals("novo@email.com", empresa.getEmail().getValue());
            assertEquals("11977777777", empresa.getTelefone().getPhone());

            verify(empresaRepository).atualizar(empresa);
        }

        @Test
        @DisplayName("Deve alterar apenas o nome quando os outros campos forem nulos")
        void deveAlterarApenasNome() {
            Empresa empresa = spy(criarEmpresa());
            String nomeAntigo = empresa.getName();
            String emailAntigo = empresa.getEmail().getValue();
            when(empresaRepository.buscaPorId(EMPRESA_ID)).thenReturn(Optional.of(empresa));

            AtualizaEmpresaCommand command = new AtualizaEmpresaCommand(
                    "Novo Nome", null, null, null, null, null, null
            );

            atualizaEmpresa.atualizar(EMPRESA_ID, command);

            assertEquals("Novo Nome", empresa.getName());
            assertEquals(emailAntigo, empresa.getEmail().getValue(), "Email não deveria mudar");

            verify(empresaRepository).atualizar(empresa);
        }

        @Test
        @DisplayName("Deve alterar apenas o email quando os outros campos forem nulos")
        void deveAlterarApenasEmail() {
            Empresa empresa = spy(criarEmpresa());
            when(empresaRepository.buscaPorId(EMPRESA_ID)).thenReturn(Optional.of(empresa));

            AtualizaEmpresaCommand command = new AtualizaEmpresaCommand(
                    null, null, null, "novo@email.com", null, null, null
            );

            atualizaEmpresa.atualizar(EMPRESA_ID, command);

            assertEquals("novo@email.com", empresa.getEmail().getValue());
            verify(empresaRepository).atualizar(empresa);
        }

        @Test
        @DisplayName("Deve alterar apenas o endereço quando todos os campos de endereço forem enviados")
        void deveAlterarApenasEndereco() {
            Empresa empresa = spy(criarEmpresa());
            when(empresaRepository.buscaPorId(EMPRESA_ID)).thenReturn(Optional.of(empresa));

            AtualizaEmpresaCommand command = new AtualizaEmpresaCommand(
                    null, null, null, null, "Rua Nova", "São Paulo", "SP"
            );

            atualizaEmpresa.atualizar(EMPRESA_ID, command);

            assertEquals("Rua Nova", empresa.getEndereco().getEndereco());
            assertEquals("São Paulo", empresa.getEndereco().getMunicipio());
            assertEquals("SP", empresa.getEndereco().getEstado());

            verify(empresaRepository).atualizar(empresa);
        }

        @Test
        @DisplayName("Não deve alterar nada quando todos os campos forem nulos")
        void naoDeveAlterarNadaQuandoTodosNulos() {
            Empresa empresa = spy(criarEmpresa());
            String nomeAntigo = empresa.getName();
            String emailAntigo = empresa.getEmail().getValue();
            when(empresaRepository.buscaPorId(EMPRESA_ID)).thenReturn(Optional.of(empresa));

            AtualizaEmpresaCommand command = new AtualizaEmpresaCommand(
                    null, null, null, null, null, null, null
            );

            atualizaEmpresa.atualizar(EMPRESA_ID, command);

            assertEquals(nomeAntigo, empresa.getName());
            assertEquals(emailAntigo, empresa.getEmail().getValue());

            verify(empresaRepository).atualizar(empresa);
        }
    }

    // ------------------------------------------------------------
    // Cenários de Erro
    // ------------------------------------------------------------

    @Nested
    @DisplayName("Cenários de Erro")
    class CenariosErro {

        @Test
        @DisplayName("Deve lançar exceção quando a empresa não for encontrada")
        void deveLancarExcecaoQuandoEmpresaNaoEncontrada() {
            when(empresaRepository.buscaPorId(EMPRESA_ID)).thenReturn(Optional.empty());

            AtualizaEmpresaCommand command = new AtualizaEmpresaCommand(
                    "Nome", null, null, null, null, null, null
            );

            EmpresaNotFound ex = assertThrows(EmpresaNotFound.class, () ->
                    atualizaEmpresa.atualizar(EMPRESA_ID, command)
            );

            assertEquals("Empresa não encontrada", ex.getMessage());
            verify(empresaRepository, never()).atualizar(any());
        }
    }
}