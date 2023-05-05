package br.com.brq.caixaeletronico.cliente;

import br.com.brq.caixaeletronico.conta.DadosCriacaoConta;

public record DadosClienteCadastro(
        String nome,
        String email,
        String telefone,
        String senha,
        DadosCriacaoConta conta) {
}
