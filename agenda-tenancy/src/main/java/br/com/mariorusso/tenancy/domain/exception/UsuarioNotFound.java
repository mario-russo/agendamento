package br.com.mariorusso.tenancy.domain.exception;

public class UsuarioNotFound extends DomainException{

    public UsuarioNotFound (String msg, int code){
        super(msg, code);
    }
}
