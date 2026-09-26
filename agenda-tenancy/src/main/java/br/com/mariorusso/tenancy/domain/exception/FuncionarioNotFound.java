package br.com.mariorusso.tenancy.domain.exception;

public class FuncionarioNotFound extends DomainException{
    public FuncionarioNotFound(String message, int code) {
        super(message, code);
    }
}
