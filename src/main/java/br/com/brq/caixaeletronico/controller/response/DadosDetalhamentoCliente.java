package br.com.brq.caixaeletronico.controller.response;

import br.com.brq.caixaeletronico.model.Cliente;
import br.com.brq.caixaeletronico.model.Conta;

public record DadosDetalhamentoCliente(
        Long id,
        String nome,
        String email,
        String telefone,
        String senha,
        Conta conta
) {
    public DadosDetalhamentoCliente (Cliente cliente) {
        this(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getTelefone(),cliente.getSenha(),
                cliente.getConta());
    }
}
