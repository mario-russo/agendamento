package br.com.mariorusso.tenancy.adapters.input;


import br.com.mariorusso.tenancy.application.ports.in.BuscaFuncionarioUseCase;
import br.com.mariorusso.tenancy.domain.Funcionario;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;

import java.util.List;

@Path("/funcionario")
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(value = MediaType.APPLICATION_JSON)
public class FuncionarioResource {

    @Inject
    BuscaFuncionarioUseCase buscaFuncionario;

    @Inject
    @Claim("empresa_id")
    ClaimValue<Long> empresaId;

    @GET
    @Path("/empresa")
    public Response buscaFuncionarioPoEmpresa() {

        List<Funcionario> funcionarios = buscaFuncionario.buscaPorEmpresa(empresaId.getValue());
        return Response.ok(funcionarios).build();
    }


}
