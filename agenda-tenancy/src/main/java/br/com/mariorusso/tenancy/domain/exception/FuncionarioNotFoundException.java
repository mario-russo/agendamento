package br.com.mariorusso.tenancy.domain.exception;

public class FuncionarioNotFoundException extends RuntimeException{
    public FuncionarioNotFoundException(String message) {
        super(message);
    }
}
