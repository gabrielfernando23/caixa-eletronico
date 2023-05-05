package br.com.brq.caixaeletronico.caixa;

public record DadosAtualizarNotas(
        Long id,
        Integer NotasDe100,
        Integer NotasDe50,
        Integer NotasDe20,
        Integer NotasDe10
) {
}
