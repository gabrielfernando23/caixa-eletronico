package br.com.brq.caixaeletronico.model;

import br.com.brq.caixaeletronico.controller.request.DadosClienteCadastro;
import jakarta.persistence.*;

@Table(name="Clientes")
@Entity(name = "Cliente")
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    @Embedded
    private Conta conta;
    private boolean ativo;


    public Cliente(DadosClienteCadastro dados) {
        this.conta = new Conta(dados.conta());
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.senha = dados.senha();
        this.ativo = true;
    }

    public Cliente() {

    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}
