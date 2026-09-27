package br.com.mariorusso.tenancy.config.globalException;

import br.com.mariorusso.tenancy.domain.exception.FuncionarioNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class FuncionarioNotFoundMapper implements ExceptionMapper<FuncionarioNotFoundException> {
    private static final Logger LOG = Logger.getLogger(FuncionarioDeOutraEmpresaMapper.class);

    @Override
    public Response toResponse(FuncionarioNotFoundException exception) {

        LOG.warnf("FuncionarioNotFoundMapper [%s]: %s",
                exception.getClass().getSimpleName(), exception.getMessage());

        ErrorResponse error = new ErrorResponse(exception.getMessage(),404);
        return Response.status(error.codigo()).entity(error).build();
    }
}
