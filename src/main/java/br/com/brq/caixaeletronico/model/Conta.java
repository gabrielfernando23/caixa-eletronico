package br.com.brq.caixaeletronico.model;

import br.com.brq.caixaeletronico.controller.request.DadosCriacaoConta;
import jakarta.persistence.*;

@Embeddable
public class Conta {
    private String numero;
    private String agencia;
    private Double saldo;

    public Conta() {
    }

    public Conta(DadosCriacaoConta dados) {
        this.numero = dados.numero();
        this.agencia = dados.agencia();
        this.saldo = dados.saldo();
    }

    public void depositar(Double valor) {
        saldo += valor;
    }

    public String getNumero() {
        return numero;
    }

    public String getAgencia() {
        return agencia;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public void sacar(Double valor) {
        saldo -= valor;
    }
}
