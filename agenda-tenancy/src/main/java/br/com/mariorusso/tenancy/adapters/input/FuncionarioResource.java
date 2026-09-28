package br.com.mariorusso.tenancy.adapters.input;


import br.com.mariorusso.tenancy.application.dtos.request.AtualizaFuncionarioDto;
import br.com.mariorusso.tenancy.application.dtos.request.SalvaFuncionarioCommand;
import br.com.mariorusso.tenancy.application.ports.in.*;
import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.Pagina;
import br.com.mariorusso.tenancy.domain.exception.FuncionarioNotFoundException;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;

@Path("/funcionarios")
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(value = MediaType.APPLICATION_JSON)
public class FuncionarioResource {

    @Inject
    BuscaFuncionarioUseCase buscaFuncionario;

    @Inject
    SalvaFuncionarioUseCase salvaFuncionario;

    @Inject
    AtualizaFuncionarioUseCase atualizaFuncionarioUseCase;

    @Inject
    DesativarFuncionarioUsecase desativaFuncionarioUseCase;

    @Inject
    BuscaFuncionarioPorIdUseCase buscaFuncionarioPorId;


    @Inject
    @Claim("empresa_id")
    ClaimValue<Long> empresaId;

    @GET
    @Authenticated
    public Response buscaFuncionarioPorEmpresa(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("50") int size
    ) {

        Pagina<Funcionario> funcionarios = buscaFuncionario.buscaPorEmpresa(
                empresaId.getValue(),
                page,
                size);
        return Response.ok(funcionarios).build();
    }

    @POST
    @Authenticated
    public Response salvaFuncionario(SalvaFuncionarioCommand dto) {

        salvaFuncionario.execute(dto.comEmpresaId(empresaId.getValue()));
        return Response.ok().build();

    }

    @PATCH
    @Path("/{id}")
    @Authenticated
    public Response atualizaFuncionario(@PathParam("id") Long id, AtualizaFuncionarioDto dto) {
        atualizaFuncionarioUseCase.atualizar(
                id,
                empresaId.getValue(),
                dto.nome(),
                dto.telefone()
        );
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Authenticated
    public Response buscaPorId(@PathParam("id") Long id) {

       Funcionario funcionario = buscaFuncionarioPorId.busca(id , empresaId.getValue())
                .orElseThrow(() -> new FuncionarioNotFoundException("Funcionário Não encontrado"));

        return Response.ok(funcionario).build();
    }

    @DELETE
    @Path("/{id}")
    @Authenticated
    public Response desativarFuncionario(@PathParam("id") Long id) {

        desativaFuncionarioUseCase.desativar(id, empresaId.getValue());

        return Response.noContent().build();
    }

}
