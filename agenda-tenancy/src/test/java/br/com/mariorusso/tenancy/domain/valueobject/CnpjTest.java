package br.com.mariorusso.tenancy.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CnpjTest {

    @Nested
    @DisplayName("Cenários de Sucesso")
    class CenariosSucesso {

        @ParameterizedTest
        @ValueSource(strings = {
                "33592510000154",         // Google (numérico puro)
                "00.623.904/0001-73",     // Apple (com máscara)
                "   33.592.510/0001-54   " // Google (com espaços em volta)
        })
        @DisplayName("Deve criar a instância com sucesso e limpar formatação quando o CNPJ for matematicamente válido")
        void deveCriarCnpjValido(String input) {
            // Act
            Cnpj cnpj = new Cnpj(input);

            // Assert
            String apenasNumerosEsperados = input.replaceAll("\\D", "");
            assertEquals(apenasNumerosEsperados, cnpj.getValue());
        }
    }

    @Nested
    @DisplayName("Cenários de Falha (Exceções)")
    class CenariosFalha {

        @ParameterizedTest
        @NullSource
        @DisplayName("Deve lançar exceção quando o valor fornecido for nulo")
        void deveLancarExcecaoQuandoNulo(String inputNull) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new Cnpj(inputNull)
            );
            assertEquals("CNPJ inválido", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "",                  // Vazio
                " ",                 // Em branco
                "3359251000015",     // Curto demais (13 dígitos)
                "335925100001544",   // Longo demais (15 dígitos)
                "12345ABC"// Contém letras inviabilizando o tamanho numérico
        })
        @DisplayName("Deve lançar exceção quando o tamanho numérico final não for exatamente 14 dígitos")
        void deveLancarExcecaoParaTamanhoIncorreto(String inputInvalido) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new Cnpj(inputInvalido)
            );
            assertEquals("CNPJ inválido", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "00000000000000",
                "11111111111111",
                "99999999999999"
        })
        @DisplayName("Deve lançar exceção quando o CNPJ for composto por números repetidos")
        void deveLancarExcecaoParaNumerosRepetidos(String repetidos) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new Cnpj(repetidos)
            );
            assertEquals("CNPJ inválido", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "33592510000100", // Estrutura do Google mas dígitos verificadores inválidos
                "00.623.904/0001-00"
        })
        @DisplayName("Deve lançar exceção se o cálculo dos dígitos verificadores falhar")
        void deveLancarExcecaoQuandoCalculoDigitoFalhar(String calculoInvalido) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new Cnpj(calculoInvalido)
            );
            assertEquals("CNPJ inválido", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Regras de Igualdade (Equals e HashCode)")
    class EqualsEHashCode {

        @Test
        @DisplayName("Deve considerar objetos iguais se os números limpos forem idênticos")
        void deveSerIgual() {
            Cnpj cnpj1 = new Cnpj("33.592.510/0001-54");
            Cnpj cnpj2 = new Cnpj("33592510000154"); // Sem máscara

            assertEquals(cnpj1, cnpj2);
            assertEquals(cnpj1.hashCode(), cnpj2.hashCode());
        }

        @Test
        @DisplayName("Deve considerar objetos diferentes se os números internos divergirem")
        void deveSerDiferente() {
            Cnpj cnpj1 = new Cnpj("33.592.510/0001-54"); // Google
            Cnpj cnpj2 = new Cnpj("00.623.904/0001-73"); // Apple

            assertNotEquals(cnpj1, cnpj2);
        }

        @Test
        @DisplayName("Deve validar verificações básicas estruturais do método equals")
        void validacoesBasicasEquals() {
            Cnpj cnpj = new Cnpj("33592510000154");

            assertEquals(cnpj, cnpj);           // Mesmo objeto em memória
            assertNotEquals(cnpj, null);        // Comparação com nulo
            assertNotEquals(cnpj, "string");    // Comparação com tipo diferente
        }
    }
}
