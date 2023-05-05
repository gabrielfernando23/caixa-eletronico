package br.com.brq.caixaeletronico.caixa;

import br.com.brq.caixaeletronico.model.CaixaEletronico;

import java.math.BigDecimal;

public record DadosDetalhamentoCaixa(
        Long id,
        Double saldoTotal,
        Integer NotasDe100,
        Integer NotasDe50,
        Integer NotasDe20,
        Integer NotasDe10
) {
    public DadosDetalhamentoCaixa(CaixaEletronico caixa) {
        this(caixa.getId(),caixa.getSaldoTotal(), caixa.getNotasDe100(),caixa.getNotasDe50(),
                caixa.getNotasDe20(), caixa.getNotasDe10());
    }
}
