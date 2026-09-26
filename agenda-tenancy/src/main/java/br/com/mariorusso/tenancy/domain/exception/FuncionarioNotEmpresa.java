package br.com.mariorusso.tenancy.domain.exception;

public class FuncionarioNotEmpresa extends DomainException{
    public FuncionarioNotEmpresa(String message, int code) {
        super(message, code);
    }
}
