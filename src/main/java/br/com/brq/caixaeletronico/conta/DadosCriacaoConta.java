package br.com.brq.caixaeletronico.conta;

import java.math.BigDecimal;

public record DadosCriacaoConta(
        String numero,
        String agencia,
        Double saldo
) {
}
