package br.com.mariorusso.tenancy.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TelefoneTest {

    @Nested
    @DisplayName("Cenários de Sucesso")
    class CenariosSucesso {

        @ParameterizedTest
        @ValueSource(strings = {"21988888888", "11977777777", "61966666666"})
        @DisplayName("Deve criar Telefone válido com 11 dígitos (Celular com DDD)")
        void deveCriarTelefoneComOnzeDigitos(String numeroValido) {
            Telefone telefone = new Telefone(numeroValido);
            assertEquals(numeroValido, telefone.getPhone());
        }

        @ParameterizedTest
        @ValueSource(strings = {"2133334444", "1122223333", "8134445555"})
        @DisplayName("Deve criar Telefone válido com 10 dígitos (Fixo com DDD)")
        void deveCriarTelefoneComDezDigitos(String numeroValido) {
            Telefone telefone = new Telefone(numeroValido);
            assertEquals(numeroValido, telefone.getPhone());
        }
    }

    @Nested
    @DisplayName("Cenários de Falha (Exceções)")
    class CenariosFalha {

        @Test
        @DisplayName("Deve lançar exceção quando o telefone for nulo")
        void deveLancarExcecaoQuandoNulo() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> new Telefone(null));
            assertEquals("Telefone inválido", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "",             // Vazio
                " ",            // Apenas espaço
                "123456789",    // Curto demais (9 dígitos)
                "123456789012", // Longo demais (12 dígitos)
                "2198888-8888", // Com hífen
                "(21)988888888",// Com parênteses
                "2198888888a",  // Com letras
                "21 988888888"  // Com espaços entre os números
        })
        @DisplayName("Deve lançar exceção para formatos inválidos")
        void deveLancarExcecaoParaFormatosInvalidos(String numeroInvalido) {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> new Telefone(numeroInvalido));
            assertEquals("Telefone inválido", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Regras de Igualdade (Equals e HashCode)")
    class EqualsEHashCode {

        @Test
        @DisplayName("Deve considerar dois telefones iguais se tiverem o mesmo número")
        void deveSerIgual() {
            Telefone tel1 = new Telefone("21988888888");
            Telefone tel2 = new Telefone("21988888888");

            assertEquals(tel1, tel2);
            assertEquals(tel1.hashCode(), tel2.hashCode());
        }

        @Test
        @DisplayName("Deve considerar telefones diferentes se os números forem distintos")
        void deveSerDiferente() {
            Telefone tel1 = new Telefone("21988888888");
            Telefone tel2 = new Telefone("2133334444");

            assertNotEquals(tel1, tel2);
            assertNotEquals(tel1.hashCode(), tel2.hashCode());
        }

        @Test
        @DisplayName("Deve validar comparações básicas do equals")
        void validacoesBasicasEquals() {
            Telefone tel = new Telefone("21988888888");

            assertEquals(tel, tel);          // Mesmo objeto
            assertNotEquals(tel, null);      // Comparando com null
            assertNotEquals(tel, "string");  // Tipos diferentes
        }
    }

}