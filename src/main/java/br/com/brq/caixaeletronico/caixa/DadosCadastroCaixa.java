package br.com.brq.caixaeletronico.caixa;

import java.math.BigDecimal;

public record DadosCadastroCaixa(
        Integer NotasDe100,
        Integer NotasDe50,
        Integer NotasDe20,
        Integer NotasDe10
) {
}
