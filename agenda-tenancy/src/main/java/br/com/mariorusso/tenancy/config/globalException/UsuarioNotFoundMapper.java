package br.com.mariorusso.tenancy.config.globalException;

import br.com.mariorusso.tenancy.domain.exception.UsuarioNotFound;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import static io.quarkus.arc.ComponentsProvider.LOG;


@Provider
public class UsuarioNotFoundMapper implements ExceptionMapper<UsuarioNotFound> {
    @Override
    public Response toResponse(UsuarioNotFound exception) {
        LOG.warnf("FuncionarioNotEmpresaMapper [%s]: %s",
                exception.getClass().getSimpleName(), exception.getMessage());

        ErrorResponse error = new ErrorResponse(exception.getMessage(),404);

        return Response.status(error.codigo()).entity(error).build();
    }
}
