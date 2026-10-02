package br.com.mariorusso.tenancy.domain;

import br.com.mariorusso.share.Endereco;
import br.com.mariorusso.tenancy.domain.valueobject.Cnpj;
import br.com.mariorusso.tenancy.domain.valueobject.Email;
import br.com.mariorusso.tenancy.domain.valueobject.Telefone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EmpresaTest {

    // CONSTANTES CORRIGIDAS: Dados reais válidos para não quebrar as regras internas dos VOs
    private static final String NOME_VALIDO = "Mário Russo LTDA";
    private static final String CNPJ_VALIDO = "33592510000154"; // Google Brasil (Matematicamente válido)
    private static final String EMAIL_VALIDO = "contato@mariorusso.com";
    private static final String TELEFONE_VALIDO = "21988888888";
    private static final Long USUARIO_ID = 1L;

    private final Endereco enderecoMock = mock(Endereco.class);

    @Nested
    @DisplayName("Cenários de Criação (Construtor)")
    class CenariosCriacao {

        @Test
        @DisplayName("Deve instanciar uma empresa ativa e com datas preenchidas ao fornecer dados válidos")
        void deveCriarEmpresaComSucesso() {
            // Act
            Empresa empresa = new Empresa(NOME_VALIDO, CNPJ_VALIDO, EMAIL_VALIDO, TELEFONE_VALIDO, enderecoMock, USUARIO_ID);

            // Assert
            assertAll(
                    () -> assertNull(empresa.getId(), "O ID inicial deve ser nulo"),
                    () -> assertEquals(NOME_VALIDO, empresa.getName()),
                    () -> assertEquals(USUARIO_ID, empresa.getUsuarioId()),
                    () -> assertEquals(enderecoMock, empresa.getEndereco()),
                    () -> assertTrue(empresa.isActive(), "A empresa deve iniciar como ativa"),
                    () -> assertNotNull(empresa.getDateCreate(), "A data de criação deve ser gerada"),
                    () -> assertNotNull(empresa.getDateUpdate(), "A data de atualização deve ser gerada"),
                    () -> assertNotNull(empresa.getFuncionarios(), "A lista de funcionários deve ser inicializada vazia"),
                    () -> assertTrue(empresa.getFuncionarios().isEmpty())
            );
        }

        @Test
        @DisplayName("Deve aplicar o trim() no nome da empresa durante a criação")
        void deveAplicarTrimNoNome() {
            // Act
            Empresa empresa = new Empresa("   Empresa Espaçada   ", CNPJ_VALIDO, EMAIL_VALIDO, TELEFONE_VALIDO, enderecoMock, USUARIO_ID);

            // Assert
            assertEquals("Empresa Espaçada", empresa.getName());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   "})
        @DisplayName("Deve lançar exceção ao tentar criar empresa com nome nulo, vazio ou em branco")
        void deveLancarExcecaoParaNomeInvalido(String nomeInvalido) {
            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new Empresa(nomeInvalido, CNPJ_VALIDO, EMAIL_VALIDO, TELEFONE_VALIDO, enderecoMock, USUARIO_ID)
            );
            assertEquals("Nome invalido", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Cenários de Gerenciamento de Funcionários")
    class GerenciamentoFuncionarios {

        @Test
        @DisplayName("Deve adicionar um funcionário na empresa com sucesso")
        void deveAdicionarFuncionarioComSucesso() {
            // Arrange
            Empresa empresa = new Empresa(NOME_VALIDO, CNPJ_VALIDO, EMAIL_VALIDO, TELEFONE_VALIDO, enderecoMock, USUARIO_ID);
            Long funcionarioId = 100L;

            // Act
            empresa.adicionaFuncionario(funcionarioId);

            // Assert
            assertTrue(empresa.getFuncionarios().contains(funcionarioId));
            assertEquals(1, empresa.getFuncionarios().size());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar adicionar um funcionário que já pertence à empresa")
        void deveLancarExcecaoParaFuncionarioDuplicado() {
            // Arrange
            Empresa empresa = new Empresa(NOME_VALIDO, CNPJ_VALIDO, EMAIL_VALIDO, TELEFONE_VALIDO, enderecoMock, USUARIO_ID);
            Long funcionarioId = 100L;
            empresa.adicionaFuncionario(funcionarioId);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    empresa.adicionaFuncionario(funcionarioId)
            );
            assertEquals("Funcionario já está na empresa", exception.getMessage());
        }

        @Test
        @DisplayName("Deve adicionar uma lista (Set) de funcionários com sucesso")
        void deveAdicionarListaDeFuncionarios() {
            // Arrange
            Empresa empresa = new Empresa(NOME_VALIDO, CNPJ_VALIDO, EMAIL_VALIDO, TELEFONE_VALIDO, enderecoMock, USUARIO_ID);
            Set<Long> novosFuncionarios = Set.of(200L, 300L, 400L);

            // Act
            empresa.adicionaFuncionarios(novosFuncionarios);

            // Assert
            assertEquals(3, empresa.getFuncionarios().size());
            assertTrue(empresa.getFuncionarios().containsAll(novosFuncionarios));
        }

        @Test
        @DisplayName("Não deve quebrar nem alterar nada ao tentar adicionar uma lista nula de funcionários")
        void naoDeveFazerNadaAoAdicionarListaNula() {
            // Arrange
            Empresa empresa = new Empresa(NOME_VALIDO, CNPJ_VALIDO, EMAIL_VALIDO, TELEFONE_VALIDO, enderecoMock, USUARIO_ID);

            // Act
            empresa.adicionaFuncionarios(null);

            // Assert
            assertTrue(empresa.getFuncionarios().isEmpty());
        }
    }

    @Nested
    @DisplayName("Cenários de Reidratação")
    class CenariosReidratar {

        @Test
        @DisplayName("Deve reidratar o estado completo da empresa vindo do banco corretamente")
        void deveReidratarEmpresaComSucesso() {
            // Arrange
            Long id = 999L;
            Cnpj cnpj = mock(Cnpj.class);
            Telefone telefone = mock(Telefone.class);
            Email email = mock(Email.class);
            Set<Long> funcionarios = Set.of(500L);
            LocalDateTime dataCriacao = LocalDateTime.now().minusDays(5);
            LocalDateTime dataAtualizacao = LocalDateTime.now();

            // Act
            Empresa empresa = Empresa.reidratar(
                    id, NOME_VALIDO, cnpj, telefone, email, enderecoMock, USUARIO_ID,
                    funcionarios, false, dataCriacao, dataAtualizacao
            );

            // Assert
            assertAll(
                    () -> assertEquals(id, empresa.getId()),
                    () -> assertEquals(NOME_VALIDO, empresa.getName()),
                    () -> assertEquals(cnpj, empresa.getCnpj()),
                    () -> assertEquals(telefone, empresa.getTelefone()),
                    () -> assertEquals(email, empresa.getEmail()),
                    () -> assertEquals(enderecoMock, empresa.getEndereco()),
                    () -> assertEquals(USUARIO_ID, empresa.getUsuarioId()),
                    () -> assertFalse(empresa.isActive(), "A reidratação deve respeitar o status inativo"),
                    () -> assertEquals(dataCriacao, empresa.getDateCreate()),
                    () -> assertEquals(dataAtualizacao, empresa.getDateUpdate()),
                    () -> assertEquals(1, empresa.getFuncionarios().size()),
                    () -> assertTrue(empresa.getFuncionarios().contains(500L))
            );
        }

        @Test
        @DisplayName("Deve inicializar uma lista vazia de funcionários ao reidratar com lista nula")
        void deveInicializarListaVaziaAoReidratarComFuncionariosNulo() {
            // Act
            Empresa empresa = Empresa.reidratar(
                    1L, NOME_VALIDO, mock(Cnpj.class), mock(Telefone.class), mock(Email.class),
                    enderecoMock, USUARIO_ID, null, true, LocalDateTime.now(), LocalDateTime.now()
            );

            // Assert
            assertNotNull(empresa.getFuncionarios(), "A lista de funcionários reidratada como nula deve virar um conjunto vazio");
            assertTrue(empresa.getFuncionarios().isEmpty());
        }
    }
    @Nested
    @DisplayName("Cenários de Alteração de Dados")
    class AlteracaoDados {

        private Empresa empresa;

        @BeforeEach
        void setUp() {
            empresa = new Empresa(NOME_VALIDO, CNPJ_VALIDO, EMAIL_VALIDO, TELEFONE_VALIDO, enderecoMock, USUARIO_ID);
        }

        // ---------- alteraName ----------

        @Test
        @DisplayName("Deve alterar o nome com sucesso")
        void deveAlterarNomeComSucesso() {
            empresa.alteraName("Novo Nome LTDA");

            assertEquals("Novo Nome LTDA", empresa.getName());
        }

        @Test
        @DisplayName("Deve aplicar trim() no nome ao alterar")
        void deveAplicarTrimAoAlterarNome() {
            empresa.alteraName("   Nome Espaçado   ");

            assertEquals("Nome Espaçado", empresa.getName());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   "})
        @DisplayName("Deve lançar exceção ao alterar nome para nulo, vazio ou em branco")
        void deveLancarExcecaoAoAlterarNomeInvalido(String nomeInvalido) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    empresa.alteraName(nomeInvalido)
            );
            assertEquals("Nome não pode ser vazio", ex.getMessage());
        }

        // ---------- alteraCnpj ----------

        @Test
        @DisplayName("Deve alterar o CNPJ com sucesso")
        void deveAlterarCnpjComSucesso() {
            String novoCnpj = "11222333000181"; // outro CNPJ válido

            empresa.alteraCnpj(novoCnpj);

            assertEquals(novoCnpj, empresa.getCnpj().getValue());
        }

        // ---------- alteraEmail ----------

        @Test
        @DisplayName("Deve alterar o e-mail com sucesso")
        void deveAlterarEmailComSucesso() {
            String novoEmail = "novo@mariorusso.com";

            empresa.alteraEmail(novoEmail);

            assertEquals(novoEmail, empresa.getEmail().getValue());
        }

        // ---------- alteraTelefone ----------

        @Test
        @DisplayName("Deve alterar o telefone com sucesso")
        void deveAlterarTelefoneComSucesso() {
            String novoTelefone = "11977777777";

            empresa.alteraTelefone(novoTelefone);

            assertEquals(novoTelefone, empresa.getTelefone().getPhone());
        }

        // ---------- alteraEndereco ----------

        @Test
        @DisplayName("Deve alterar o endereço com sucesso")
        void deveAlterarEnderecoComSucesso() {
            empresa.alteraEndereco("Rua Nova", "São Paulo", "SP");

            Endereco novoEndereco = empresa.getEndereco();
            assertEquals("Rua Nova", novoEndereco.getEndereco());
            assertEquals("São Paulo", novoEndereco.getMunicipio());
            assertEquals("SP", novoEndereco.getEstado());
        }

        // ---------- dateUpdate ----------

        @Test
        @DisplayName("Deve atualizar dateUpdate ao alterar qualquer campo")
        void deveAtualizarDateUpdateAoAlterar() throws InterruptedException {
            LocalDateTime antes = empresa.getDateUpdate();
            Thread.sleep(10); // garante que o timestamp mude

            empresa.alteraName("Nome Novo");

            assertTrue(empresa.getDateUpdate().isAfter(antes),
                    "dateUpdate deve ser atualizado após alteração");
        }
    }
}
