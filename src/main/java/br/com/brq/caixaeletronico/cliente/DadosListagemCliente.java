package br.com.brq.caixaeletronico.cliente;

import br.com.brq.caixaeletronico.conta.DadosDetalhamentoConta;
import br.com.brq.caixaeletronico.model.Cliente;

public record DadosListagemCliente(
        Long id,
        String nome,
        String email,
        String telefone,
        DadosDetalhamentoConta conta
) {
    public DadosListagemCliente(Cliente cliente) {
        this(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getTelefone(),
                new DadosDetalhamentoConta(cliente.getConta()));
    }
}
