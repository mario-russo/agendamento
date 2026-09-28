package br.com.mariorusso.tenancy.application.usecase;

import br.com.mariorusso.tenancy.application.ports.out.FuncionarioRepository;
import br.com.mariorusso.tenancy.domain.Funcionario;
import br.com.mariorusso.tenancy.domain.Pagina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscaFuncionarioImplTest {
    @Mock
    private FuncionarioRepository funcionarioRepository;

    private BuscaFuncionarioImpl buscaFuncionario;

    @BeforeEach
    void setUp() {
        buscaFuncionario = new BuscaFuncionarioImpl(funcionarioRepository);
    }

    @Test
    @DisplayName("Deve retornar uma página de funcionários para a empresa informada")
    void deveRetornarPaginaDeFuncionariosComSucesso() {
        Long empresaId = 10L;
        int pagina = 0;
        int tamanho = 10;
        long totalElementos = 1L;

        Funcionario funcionario = new Funcionario("Mário Russo", "21988888888", empresaId);
        List<Funcionario> listaFuncionarios = List.of(funcionario);

        Pagina<Funcionario> paginaMock = new Pagina<>(listaFuncionarios, pagina, tamanho, totalElementos);

        when(funcionarioRepository.buscaPorEmpresaPorPagina(empresaId, pagina, tamanho))
                .thenReturn(paginaMock);

        Pagina<Funcionario> resultado = buscaFuncionario.buscaPorEmpresa(empresaId, pagina, tamanho);

        assertNotNull(resultado, "A página retornada não deveria ser nula");
        assertEquals(pagina, resultado.getPaginaAtual());
        assertEquals(tamanho, resultado.getTamanhoPagina());
        assertEquals(totalElementos, resultado.getTotalElementos());
        assertFalse(resultado.getConteudo().isEmpty());
        assertEquals("Mário Russo", resultado.getConteudo().get(0).getName());

        verify(funcionarioRepository, times(1))
                .buscaPorEmpresaPorPagina(empresaId, pagina, tamanho);
    }
}