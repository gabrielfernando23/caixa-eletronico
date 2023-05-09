package br.com.brq.caixaeletronico.controller.request;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroCaixa(
        @NotNull
        Integer NotasDe100,
        @NotNull
        Integer NotasDe50,
        @NotNull
        Integer NotasDe20,
        @NotNull
        Integer NotasDe10
) {
}
