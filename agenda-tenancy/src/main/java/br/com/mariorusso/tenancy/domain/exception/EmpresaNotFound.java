package br.com.mariorusso.tenancy.domain.exception;

public class EmpresaNotFound extends RuntimeException{

    public EmpresaNotFound(String message) {
        super(message);
    }
}
