package br.com.mariorusso.tenancy.config.globalException;

import br.com.mariorusso.tenancy.domain.exception.FuncionarioDeOutraEmpresaException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class FuncionarioDeOutraEmpresaMapper implements ExceptionMapper<FuncionarioDeOutraEmpresaException> {

    private static final Logger LOG = Logger.getLogger(FuncionarioDeOutraEmpresaMapper.class);

    @Override
    public Response toResponse(FuncionarioDeOutraEmpresaException exception) {

        LOG.warnf("FuncionarioNotEmpresaMapper [%s]: %s",
                exception.getClass().getSimpleName(), exception.getMessage());

        ErrorResponse error = new ErrorResponse(exception.getMessage(),403);
        return Response.status(error.codigo()).entity(error.messagem()).build();
    }
}
