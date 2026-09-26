package br.com.mariorusso.tenancy.domain.valueobject;

import java.util.Objects;

public class Cnpj {

    private final String value;

    // Arrays de pesos oficiais do CNPJ
    private static final int[] PESOS_FIRST_DIGIT = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESOS_SECOND_DIGIT = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public Cnpj(String cnpj) {
        this.value = validateAndSet(cnpj);
    }

    public String getValue() {
        return value;
    }

    private String validateAndSet(String cnpj) {
        if (cnpj == null) {
            throw new IllegalArgumentException("CNPJ inválido");
        }

        String apenasNumeros = cnpj.replaceAll("\\D", "");

        if (apenasNumeros.length() != 14 || isNumerosRepetidos(apenasNumeros) || !isValidCnpjCalc(apenasNumeros)) {
            throw new IllegalArgumentException("CNPJ inválido");
        }

        return apenasNumeros;
    }

    private boolean isNumerosRepetidos(String cnpj) {
        return cnpj.matches("(\\d)\\1{13}");
    }

    private boolean isValidCnpjCalc(String cnpj) {
        // 1º Dígito Verificador
        int soma1 = 0;
        for (int i = 0; i < 12; i++) {
            soma1 += (cnpj.charAt(i) - '0') * PESOS_FIRST_DIGIT[i];
        }
        int resto1 = soma1 % 11;
        int digito1 = (resto1 < 2) ? 0 : 11 - resto1;

        // 2º Dígito Verificador
        int soma2 = 0;
        for (int i = 0; i < 13; i++) {
            soma2 += (cnpj.charAt(i) - '0') * PESOS_SECOND_DIGIT[i];
        }
        int resto2 = soma2 % 11;
        int digito2 = (resto2 < 2) ? 0 : 11 - resto2;

        // Valida se os dígitos calculados batem com os dígitos reais (posições 12 e 13)
        return (cnpj.charAt(12) - '0' == digito1) && (cnpj.charAt(13) - '0' == digito2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cnpj other = (Cnpj) obj;
        return Objects.equals(value, other.value);
    }
}
