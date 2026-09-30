package br.com.mariorusso.tenancy.adapters.input;


import br.com.mariorusso.tenancy.application.command.request.AtualizaEmpresaCommand;
import br.com.mariorusso.tenancy.application.command.request.EmpresaRequest;
import br.com.mariorusso.tenancy.application.ports.in.AtualizaEmpresaUseCase;
import br.com.mariorusso.tenancy.application.ports.out.EmpresaRepository;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;

@Path("/empresas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpresaResource {

    @Inject
    EmpresaRepository repository;

    @Inject
    AtualizaEmpresaUseCase atualizarEmpresa;

    @Inject
    @Claim("empresa_id")
    ClaimValue<Long> empresaId;



    @PUT
    @Authenticated
    public Response atualizaEmpresa( @Valid AtualizaEmpresaCommand request) {
        atualizarEmpresa.atualizar(empresaId.getValue(), request);
        return Response.ok().build();
    }
}
