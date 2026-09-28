package br.com.mariorusso.tenancy.domain.exception;

public class FuncionarioDeOutraEmpresaException extends RuntimeException{
    public FuncionarioDeOutraEmpresaException(String message) {
        super(message);
    }
}
