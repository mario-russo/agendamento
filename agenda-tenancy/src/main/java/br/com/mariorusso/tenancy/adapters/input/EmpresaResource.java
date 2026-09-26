package br.com.mariorusso.tenancy.adapters.input;


import br.com.mariorusso.tenancy.application.dtos.request.EmpresaRequest;
import br.com.mariorusso.tenancy.application.ports.out.EmpresaRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/empresa")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpresaResource {

    @Inject
    EmpresaRepository repository;

    @POST
    @Path("/cadastra")
    public Response cadastraEmpresa(EmpresaRequest request) {
        repository.cadastra(request);

        return Response.ok().build();
    }
}
