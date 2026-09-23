package br.com.mariorusso.tenancy.application.dtos.request;

import br.com.mariorusso.share.Endereco;
import br.com.mariorusso.tenancy.domain.Empresa;

public record EmpresaRequest(
        String name,
        String cnpj,
        String email,
        String telefone,
        String enderecoLogradouro,
        String enderecoMunicipio,
        String enderecoEstado,
        Long usuarioId
) {
    public  Empresa toDomain(){
       Endereco enderecoDominio = new br.com.mariorusso.share.Endereco(
                this.enderecoLogradouro,
                this.enderecoMunicipio,
                this.enderecoEstado
        );
        return new Empresa( this.name,
                this.cnpj,
                this.email,
                this.telefone,
                enderecoDominio,
                this.usuarioId
        );
    }
}
