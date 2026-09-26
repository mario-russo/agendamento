package br.com.mariorusso.tenancy.adapters.input;


import br.com.mariorusso.tenancy.application.dtos.request.AtualizaFuncionarioDto;
import br.com.mariorusso.tenancy.application.dtos.request.FuncionarioRequestDto;
import br.com.mariorusso.tenancy.application.ports.in.AtualizaFuncionarioUseCase;
import br.com.mariorusso.tenancy.application.ports.in.BuscaFuncionarioUseCase;
import br.com.mariorusso.tenancy.application.ports.in.DesativarFuncionarioUsecase;
import br.com.mariorusso.tenancy.application.ports.in.SalvaFuncionarioUseCase;
import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;

import java.util.List;
import java.util.Objects;

@Path("/funcionario")
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
    FuncionarioRepository funcionarioRepository;

    @Inject
    @Claim("empresa_id")
    ClaimValue<Long> empresaId;

    @GET
    @Path("/empresa")
    @Authenticated
    public Response buscaFuncionarioPoEmpresa() {

        List<Funcionario> funcionarios = buscaFuncionario.buscaPorEmpresa(empresaId.getValue());
        return Response.ok(funcionarios).build();
    }

    @POST
    @Authenticated
    @Transactional
    public Response salvaFunciocanrio(FuncionarioRequestDto dto) {

        salvaFuncionario.execute(dto.comEmpresaId(empresaId.getValue()));

        return Response.ok().build();

    }

    @PATCH
    @Authenticated
    public Response autualizaFuncionario(AtualizaFuncionarioDto dto) {
        atualizaFuncionarioUseCase.atualizar(
                dto.nome(),
                dto.telefone(),
                dto.id(),
                empresaId.getValue());
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Authenticated
    public Response buscaPorId(@PathParam("id") Long id) {
        Funcionario funcionario = funcionarioRepository.buscaPorId(id);

        if (funcionario == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }


        if (!Objects.equals(funcionario.getEmpresaId(), empresaId.getValue())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
