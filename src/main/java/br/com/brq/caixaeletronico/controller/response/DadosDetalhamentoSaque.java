package br.com.brq.caixaeletronico.controller.response;

import br.com.brq.caixaeletronico.model.CaixaEletronico;
import br.com.brq.caixaeletronico.model.Cliente;
import br.com.brq.caixaeletronico.model.Conta;

public record DadosDetalhamentoSaque(
        Double saldoAtual,
        Double ValorSaque,
        Integer quantiaNotas100Entregues,
        Integer quantiaNotas50Entregues,
        Integer quantiaNotas20Entregues,
        Integer quantiaNotas10Entregues
) {

}
