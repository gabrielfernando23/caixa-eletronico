package br.com.brq.caixaeletronico.controller.response;

import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoConta;
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
