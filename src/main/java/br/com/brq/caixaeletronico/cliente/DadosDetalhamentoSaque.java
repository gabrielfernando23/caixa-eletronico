package br.com.brq.caixaeletronico.cliente;

import br.com.brq.caixaeletronico.model.CaixaEletronico;
import br.com.brq.caixaeletronico.model.Cliente;
import br.com.brq.caixaeletronico.model.Conta;

public record DadosDetalhamentoSaque(
        Long idCliente,
        String nome,
        Conta conta,
        Long idCaixa,
        Double ValorSaque
) {
    public DadosDetalhamentoSaque(Cliente cliente, CaixaEletronico caixa, double valor) {
        this(cliente.getId(), cliente.getNome(), cliente.getConta(), caixa.getId(),valor);
    }
}
