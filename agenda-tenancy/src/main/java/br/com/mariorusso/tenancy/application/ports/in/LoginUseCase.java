package br.com.mariorusso.tenancy.application.ports.in;

import br.com.mariorusso.tenancy.application.command.Token;

public interface LoginUseCase {
    Token exec (String email, String password);
}
