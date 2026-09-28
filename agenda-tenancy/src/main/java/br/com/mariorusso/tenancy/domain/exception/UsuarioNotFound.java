package br.com.mariorusso.tenancy.domain.exception;

public class UsuarioNotFound extends RuntimeException{

    public UsuarioNotFound (String msg){
        super(msg);
    }
}
