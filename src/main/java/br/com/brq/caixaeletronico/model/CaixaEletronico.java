package br.com.brq.caixaeletronico.model;

import br.com.brq.caixaeletronico.ValidacaoException;
import br.com.brq.caixaeletronico.caixa.DadosAtualizarNotas;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Table(name = "Caixas")
@Entity(name = "Caixa")
public class CaixaEletronico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double saldoTotal;
    private Integer notasDe100;
    private Integer notasDe50;
    private Integer notasDe20;
    private Integer notasDe10;

    public CaixaEletronico() {
    }

    public CaixaEletronico( Integer notasDe100, Integer notasDe50, Integer notasDe20
            , Integer notasDe10) {
        this.notasDe100 = notasDe100;
        this.notasDe50 = notasDe50;
        this.notasDe20 = notasDe20;
        this.notasDe10 = notasDe10;
        saldoTotal =
                Double.valueOf((notasDe100*100) + (notasDe20*20) + (notasDe50*50) + (notasDe10*10));
    }

    public void calcularSaldo() {
        saldoTotal = Double.valueOf((notasDe100*100) + (notasDe20*20) + (notasDe50*50) + (notasDe10*10));
    }

    public boolean sacar(Double valor,Cliente cliente) {
        if (cliente.getConta().getSaldo() >= valor){
            if (valor <= this.saldoTotal) {
                if (valor % 10 ==0){
                    saque100(valor);
                    calcularSaldo();
                    return true;
                }else{
                    throw new ValidacaoException("Valor inválido");
                }
            } else {
                throw new ValidacaoException("Não há notas disponíveis para realizar este saque");

            }
        } else{
            throw new ValidacaoException("Saldo insuficiente");
        }

    }


    public void saque100(double valor) {
        int qtdNotasNecessarias = (int) (valor / 100);
        if (notasDe100 > 0 && valor % 100 == 0) {
            if (qtdNotasNecessarias <= notasDe100){
                notasDe100 -= qtdNotasNecessarias;
                valor -= (qtdNotasNecessarias * 100);
                saldoTotal -= (qtdNotasNecessarias * 100);
            }
            else {
                valor -= (notasDe100 * 100);
                saldoTotal -= (notasDe100 * 100);
                notasDe100 = (Integer) 0;
                saque50(valor);
            }
        }else if(valor > 100 && notasDe100 > 0 && (notasDe100 <= (valor / 100))) {
            valor -= (notasDe100 * 100);
            saldoTotal -= (notasDe100 * 100);
            notasDe100 = (Integer) 0;
            saque50(valor);
        }else if(valor > 100 && notasDe100 > 0 && (notasDe100 >= (valor / 100))){
            notasDe100 -= qtdNotasNecessarias;
            valor -= (qtdNotasNecessarias * 100);
            saldoTotal -= (qtdNotasNecessarias * 100);
            saque50(valor);
        }
        else{
            saque50(valor);
        }
    }

    private void saque50(double valor) {
        int qtdNotasNecessarias = (int) (valor / 50);
        if (notasDe50 > 0 && valor % 50 == 0 && valor > 0) {
            if (qtdNotasNecessarias <= notasDe50) {
                notasDe50 -= qtdNotasNecessarias;
                valor -= (qtdNotasNecessarias * 50);
                saldoTotal -= valor;
            } else {
                    valor -= (notasDe50 * 50);
                    saldoTotal -= (notasDe50 * 50);
                    notasDe50 = (Integer) 0;
                    saque20(valor);
            }

        }else if(valor > 50 && notasDe50 > 0 && (notasDe50 <= (valor / 50))) {
            valor -= (notasDe50 * 50);
            saldoTotal -= (notasDe50 * 50);
            notasDe50 = (Integer) 0;
            saque20(valor);
        }else if(valor > 50 && notasDe50 > 0 && (notasDe50 >= (valor / 50))){
            notasDe50 -= qtdNotasNecessarias;
            valor -= (qtdNotasNecessarias * 50);
            saldoTotal -= (qtdNotasNecessarias * 50);
            saque20(valor);
        }
        else {
            saque20(valor);
        }
    }

    private void saque20(double valor) {
        int qtdNotasNecessarias = (int) (valor / 20);
        if (notasDe20 > 0 && valor % 20 == 0 && valor > 0) {
            if (qtdNotasNecessarias <= notasDe20) {
                notasDe20 -= qtdNotasNecessarias;
                valor -= (qtdNotasNecessarias * 20);
                saldoTotal -= (qtdNotasNecessarias * 20);
            } else {
                valor -= (notasDe20 * 20);
                saldoTotal -= (notasDe20 * 20);
                notasDe20 = (Integer) 0;
                saque10(valor);
            }
        }else if(valor > 20 && notasDe20 > 0 && (notasDe20 <= (valor / 20))) {
            valor -= (notasDe20 * 20);
            saldoTotal -= (notasDe20 * 20);
            notasDe20 = (Integer) 0;
            saque10(valor);
        }else if(valor > 20 && notasDe20 > 0 && (notasDe20 >= (valor / 20))){
            notasDe20 -= qtdNotasNecessarias;
            valor -= (qtdNotasNecessarias * 20);
            saldoTotal -= (qtdNotasNecessarias * 20);
            saque10(valor);
        }
        else {
            saque10(valor);
        }
    }

    private void saque10(double valor) {
        if (notasDe10 > 0 && valor % 10 == 0 && valor > 0) {
            int qtdNotasNecessarias = (int) (valor / 10);
            if (qtdNotasNecessarias <= notasDe10){
                notasDe10 -= qtdNotasNecessarias;
                valor -= (qtdNotasNecessarias * 10);
                saldoTotal -= (qtdNotasNecessarias * 10);
            }else {
                valor -= (notasDe10 * 10);
                saldoTotal -= (notasDe10 * 10);
                notasDe10 = (Integer) 0;
            }
        }
    }

    public Long getId() {
        return id;
    }

    public Double getSaldoTotal() {
        calcularSaldo();
        return saldoTotal;
    }

    public Integer getNotasDe100() {
        return notasDe100;
    }

    public Integer getNotasDe50() {
        return notasDe50;
    }

    public Integer getNotasDe20() {
        return notasDe20;
    }

    public Integer getNotasDe10() {
        return notasDe10;
    }

    public void setSaldoTotal(Double saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public void atualizarInformacoes(DadosAtualizarNotas dados){
        if ((100 - notasDe10) > 0) {
            int disponivel = 100 - notasDe10;
            if (dados.NotasDe10() > disponivel){
                notasDe10 += disponivel;
            }
            else{
                notasDe10 += dados.NotasDe10();
            }
        } else{
            throw new ValidacaoException("O número máximo de notas armazenadas neste caixa " +
                    "eletrônico é de 100 notas.\n Já estão armazenadas " + notasDe10 + " notas e " +
                    "você está tentando depositar " + dados.NotasDe10());
        }
        if (dados.NotasDe20() != null && (100 - notasDe20) > 0) {
            int disponivel = 100 - notasDe20;
            if (dados.NotasDe20() > disponivel){
                notasDe20 += disponivel;
            }
            else{
                notasDe20 += dados.NotasDe20();
            }
        } else{
            throw new ValidacaoException("O número máximo de notas armazenadas neste caixa " +
                    "eletrônico é de 100 notas.\n Já estão armazenadas " + notasDe20 + " notas e " +
                    "você está tentando depositar " + dados.NotasDe20());
        }
        if (dados.NotasDe50() != null && dados.NotasDe20() != null && (100 - notasDe50) > 0) {
            int disponivel = 100 - notasDe50;
            if (dados.NotasDe50() > disponivel){
                notasDe50 += disponivel;
            }
            else{
                notasDe50 += dados.NotasDe50();
            }
        } else{
            throw new ValidacaoException("O número máximo de notas armazenadas neste caixa " +
                    "eletrônico é de 100 notas.\n Já estão armazenadas " + notasDe50 + " notas e " +
                    "você está tentando depositar " + dados.NotasDe50());
        }
        if (dados.NotasDe100() != null && (100 - notasDe100) > 0) {
            int disponivel = 100 - notasDe100;
            if (dados.NotasDe100() > disponivel){
                notasDe100 += disponivel;
            }
            else{
                notasDe100 += dados.NotasDe100();
            }
        } else{
            throw new ValidacaoException("O número máximo de notas armazenadas neste caixa " +
                    "eletrônico é de 100 notas.\n Já estão armazenadas " + notasDe100 + " " +
                    "notas e" +
                    " " +
                    "você está tentando depositar " + dados.NotasDe100());
        }

        int soma10 = notasDe10 * 10;
        int soma20 = notasDe20 * 20;
        int soma50 = notasDe50 * 50;
        int soma100 = notasDe100 * 100;


        setSaldoTotal(Double.valueOf(soma10+soma20+soma50+soma100));
    }
}
