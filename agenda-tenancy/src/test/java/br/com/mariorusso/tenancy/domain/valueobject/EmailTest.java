package br.com.mariorusso.tenancy.domain.valueobject;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;



import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailTest {

    @Nested
    @DisplayName("Cenários de Sucesso")
    class CenariosSucesso {

        @ParameterizedTest
        @ValueSource(strings = {
                "mario@russo.com",
                "mario.russo@empresa.com.br",
                "usuario_123+teste@provedor.org"
        })
        @DisplayName("Deve criar a instância com sucesso para e-mails válidos")
        void deveCriarEmailValido(String input) {
            // Act
            Email email = new Email(input);

            // Assert
            assertEquals(input.trim().toLowerCase(), email.getValue());
        }

        @Test
        @DisplayName("Deve aplicar toLowerCase() no valor armazenado")
        void deveNormalizarEmail() {
            // Arrange
            String emailComCaps = "MARIO@Russo.COM"; // Removidos os espaços em branco das pontas

            // Act
            Email email = new Email(emailComCaps);

            // Assert
            assertEquals("mario@russo.com", email.getValue(), "O e-mail deve ser salvo em letras minúsculas");
        }

    }

    @Nested
    @DisplayName("Cenários de Falha (Exceções)")
    class CenariosFalha {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {
                " ",                  // Apenas espaços
                "mario.com",          // Falta o @
                "mario@com",          // Falta o domínio/extensão
                "mario@russo.",       // Falta a extensão após o ponto
                "@provedor.com",      // Falta a parte local (antes do @)
                "mario@russo.c",      // Extensão curta demais (menos de 2 caracteres)
                "mario@russo.ferramentas" // Extensão longa demais (mais de 6 caracteres no REGEX)
        })
        @DisplayName("Deve lançar exceção para e-mails nulos, vazios ou fora do padrão regex")
        void deveLancarExcecaoParaEmailInvalido(String inputInvalido) {
            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new Email(inputInvalido)
            );
            assertEquals("E-mail inválido", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Regras de Igualdade (Equals e HashCode)")
    class EqualsEHashCode {

        @Test
        @DisplayName("Deve considerar objetos iguais mesmo se foram criados com capitulações diferentes")
        void deveSerIgual() {
            // Arrange
            Email email1 = new Email("mario@russo.com");
            Email email2 = new Email("MARIO@russo.com");

            // Assert
            assertEquals(email1, email2);
            assertEquals(email1.hashCode(), email2.hashCode());
        }

        @Test
        @DisplayName("Deve considerar objetos diferentes se os endereços divergirem")
        void deveSerDiferente() {
            // Arrange
            Email email1 = new Email("mario@russo.com");
            Email email2 = new Email("outro@russo.com");

            // Assert
            assertNotEquals(email1, email2);
        }

        @Test
        @DisplayName("Deve validar verificações básicas estruturais do método equals")
        void validacoesBasicasEquals() {
            // Arrange
            Email email = new Email("mario@russo.com");

            // Assert
            assertEquals(email, email);           // Mesmo objeto em memória
            assertNotEquals(email, null);        // Comparação com nulo
            assertNotEquals(email, "string");    // Comparação com tipo diferente
        }
    }

}