package br.com.mariorusso.tenancy.adapters.input;

import br.com.mariorusso.tenancy.application.command.LoginRequest;
import br.com.mariorusso.tenancy.application.command.Token;
import br.com.mariorusso.tenancy.application.command.UsuarioRequest;
import br.com.mariorusso.tenancy.application.ports.in.LoginUseCase;
import br.com.mariorusso.tenancy.application.ports.in.RegistrarUsuarioUseCase;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResoucer {

    private final LoginUseCase login;

    private final RegistrarUsuarioUseCase registraUsuairo;

    public AuthResoucer(LoginUseCase login, RegistrarUsuarioUseCase registra) {
        this.login = login;
        this.registraUsuairo = registra;
    }

    @POST
    @Path("/login")
    public Response LoginUsuario(LoginRequest request) {

        Token token = login.exec(request.email(), request.password());
        return Response.ok(token).build();
    }

    @POST
    @Path("/register")
    @Transactional
    public Response registe(UsuarioRequest request) {
        registraUsuairo.registrar(request);
        return Response.ok().build();
    }
}
