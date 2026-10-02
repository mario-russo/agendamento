package br.com.mariorusso.tenancy.domain.exception;

public class EmpresaNaoPertenceUsuario extends RuntimeException{
    public EmpresaNaoPertenceUsuario(String message) {
        super(message);
    }
}
