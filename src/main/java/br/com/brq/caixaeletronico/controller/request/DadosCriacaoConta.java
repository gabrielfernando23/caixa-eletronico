package br.com.brq.caixaeletronico.controller.request;

import java.math.BigDecimal;

public record DadosCriacaoConta(
        String numero,
        String agencia,
        Double saldo
) {
}
