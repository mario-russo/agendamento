package br.com.mariorusso.tenancy.config.globalException;

import br.com.mariorusso.tenancy.domain.exception.DomainException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;


//classe esta em desuso
@Provider
public class GlobalExceptionMapper {
    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);
    @ServerExceptionMapper
    public Response mapDomainException(DomainException exception) {
        LOG.warnf("DomainException [%s]: %s",
                exception.getClass().getSimpleName(), exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST) 
                .entity(new ErrorResponse(exception.getMessage(), exception.getCode()))
                .build();
    }

    //
    @ServerExceptionMapper
    public Response mapGenericException(Throwable exception) {

        LOG.error("Erro não tratado na aplicação", exception);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Erro interno do Servidor", 500))
                .build();
    }
}
