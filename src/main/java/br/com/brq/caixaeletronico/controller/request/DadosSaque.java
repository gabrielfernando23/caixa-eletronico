package br.com.brq.caixaeletronico.controller.request;


import jakarta.validation.constraints.NotNull;

public record DadosSaque(
        @NotNull
        Long idCliente,
        @NotNull
        Long idCaixa,
        @NotNull
        Double valor

) {
    public DadosSaque(Long idCliente, Long idCaixa, Double valor) {
        this.idCliente = idCliente;
        this.idCaixa = idCaixa;
        this.valor = valor;
    }
}
