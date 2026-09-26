package br.com.mariorusso.tenancy.domain.exception;

public abstract class DomainException extends RuntimeException{
    private int code;

    public int getCode() {
        return code;
    }

    public DomainException(String message , int code) {
        super(message);
        this.code = code;
    }



    protected DomainException() {
    }
}
