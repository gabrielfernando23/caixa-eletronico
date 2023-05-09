package br.com.brq.caixaeletronico.controller.request;

import jakarta.validation.constraints.NotNull;

public record DadosDeposito(
        @NotNull
        Long idCliente,
        @NotNull
        Double valor
) {

}
