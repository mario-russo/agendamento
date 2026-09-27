package br.com.mariorusso.tenancy.domain;

import java.util.List;

public class Pagina <T>{
    private final List<T> conteudo;
    private final int paginaAtual;
    private final int tamanhoPagina;
    private final long totalElementos;
    private final int totalPaginas;

    public Pagina(List<T> conteudo, int paginaAtual, int tamanhoPagina, long totalElementos) {
        this.conteudo = conteudo;
        this.paginaAtual = paginaAtual;
        this.tamanhoPagina = tamanhoPagina;
        this.totalElementos = totalElementos;
        this.totalPaginas = (int) Math.ceil((double) totalElementos / tamanhoPagina);
    }

    // Getters apenas
    public List<T> getConteudo() { return conteudo; }
    public int getPaginaAtual() { return paginaAtual; }
    public int getTamanhoPagina() { return tamanhoPagina; }
    public long getTotalElementos() { return totalElementos; }
    public int getTotalPaginas() { return totalPaginas; }
}
