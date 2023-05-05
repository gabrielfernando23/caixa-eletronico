package br.com.brq.caixaeletronico.cliente;


public record DadosSaque(
        Long idCliente,
        Long idCaixa,
        Double valor

) {
    public DadosSaque(Long idCliente, Long idCaixa, Double valor) {
        this.idCliente = idCliente;
        this.idCaixa = idCaixa;
        this.valor = valor;
    }
}
