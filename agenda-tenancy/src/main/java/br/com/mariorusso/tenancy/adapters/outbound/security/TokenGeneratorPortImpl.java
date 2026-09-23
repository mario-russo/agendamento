package br.com.mariorusso.tenancy.adapters.outbound.security;

import br.com.mariorusso.tenancy.adapters.outbound.entity.EmpresaEntity;
import br.com.mariorusso.tenancy.application.ports.ou.EmpresaRepository;
import br.com.mariorusso.tenancy.application.ports.ou.TokenGeneratorPort;
import br.com.mariorusso.tenancy.domain.Empresa;
import br.com.mariorusso.tenancy.domain.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class TokenGeneratorPortImpl implements TokenGeneratorPort {


    @Inject
    EmpresaRepository empresaRepository;

    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String ACCESS_TOKEN = "access";
    private static final String REFRESH_TOKEN = "refresh";

    @ConfigProperty(name = "jwt.issuer", defaultValue = "mario-russo")
    String issuer;

    @ConfigProperty(name = "jwt.expiration", defaultValue = "3600")
    Long expiration; // 1 hora em segunds

    @ConfigProperty(name = "jwt.refresh-expiration", defaultValue = "86400")
    Long refreshExpiration; // 24 horas em segunds

    @Override
    public String generateToken(Usuario usuario) {
        if (usuario == null)
            throw new IllegalArgumentException("Usuário invalido");
        EmpresaEntity empresaEntity = EmpresaEntity.find("usuarioId", usuario.getId()).firstResult();
        Set<String> roles = usuario.getRole().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());


        return Jwt.issuer(issuer)
                .subject(usuario.getId().toString())
                .upn(usuario.getEmail().getValue())
                .claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN)
                .claim("user_id", usuario.getId())
                .claim("email", usuario.getEmail().getValue())
                .claim("name", usuario.getName())
                .claim("tipo", usuario.getTipo().toString())
                .claim("empresa_id", empresaEntity.id)
                .groups(roles)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(expiration))
                .sign();
    }

    @Override
    public String generateRefreshToken(Usuario usuario) {
        if (usuario == null)
            throw new IllegalArgumentException("Usuário invalido");

        return Jwt.issuer(issuer)
                .subject(usuario.getEmail().getValue())
                .upn(usuario.getEmail().getValue())
                .claim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN)
                .claim("email", usuario.getEmail().getValue())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(refreshExpiration))
                .sign();
    }
}
