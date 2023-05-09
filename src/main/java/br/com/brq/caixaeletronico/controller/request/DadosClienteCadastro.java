package br.com.brq.caixaeletronico.controller.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosClienteCadastro(
        @NotBlank
        String nome,
        @NotBlank
        String email,
        @NotBlank
        String telefone,
        @NotBlank
        String senha,
        @NotNull
        DadosCriacaoConta conta) {
}
