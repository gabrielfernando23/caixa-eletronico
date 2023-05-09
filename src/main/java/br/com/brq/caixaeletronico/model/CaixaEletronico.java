package br.com.brq.caixaeletronico.model;

import jakarta.persistence.*;

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

    public void setNotasDe100(Integer notasDe100) {
        this.notasDe100 = notasDe100;
    }

    public void setNotasDe50(Integer notasDe50) {
        this.notasDe50 = notasDe50;
    }

    public void setNotasDe20(Integer notasDe20) {
        this.notasDe20 = notasDe20;
    }

    public void setNotasDe10(Integer notasDe10) {
        this.notasDe10 = notasDe10;
    }


}
