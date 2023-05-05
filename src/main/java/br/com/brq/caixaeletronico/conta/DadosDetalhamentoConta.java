package br.com.brq.caixaeletronico.conta;

import br.com.brq.caixaeletronico.model.Cliente;
import br.com.brq.caixaeletronico.model.Conta;

import java.math.BigDecimal;

public record DadosDetalhamentoConta(
        String numero,
        String agencia,
        Double saldo
) {
    public DadosDetalhamentoConta (Conta conta) {
        this(conta.getNumero(), conta.getAgencia(), conta.getSaldo());
    }
}
