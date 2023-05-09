package br.com.brq.caixaeletronico.services;

import br.com.brq.caixaeletronico.exception.ValidacaoException;
import br.com.brq.caixaeletronico.controller.request.DadosAtualizarNotas;
import br.com.brq.caixaeletronico.controller.request.DadosCadastroCaixa;
import br.com.brq.caixaeletronico.controller.request.DadosDeposito;
import br.com.brq.caixaeletronico.controller.request.DadosSaque;
import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoCaixa;
import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoSaque;
import br.com.brq.caixaeletronico.model.CaixaEletronico;
import br.com.brq.caixaeletronico.model.Cliente;
import br.com.brq.caixaeletronico.repository.CaixaEletronicoRepository;
import br.com.brq.caixaeletronico.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@EnableJpaRepositories(basePackages = "br.com.brq.caixaeletronico.repository")
public class CaixaEletronicoServices {
    @Autowired
    private CaixaEletronicoRepository repository;
    @Autowired
    private ClienteRepository clienteRepository;


    public CaixaEletronico cadastrarCaixa(DadosCadastroCaixa dados) {
        CaixaEletronico caixa = new CaixaEletronico(dados.NotasDe100(), dados.NotasDe50(), dados.NotasDe20(), dados.NotasDe10());
        repository.save(caixa);
        return caixa;
    }

    public DadosDetalhamentoCaixa mostrarNotas(Long id) {
        return repository.findById(id).map(DadosDetalhamentoCaixa::new).get();
    }

    public CaixaEletronico atualizarNotas(DadosAtualizarNotas dados) {
        CaixaEletronico caixa =repository.getReferenceById(dados.id());
        if ((100 - caixa.getNotasDe100()) > 0) {
            int disponivel = 100 - caixa.getNotasDe10();
            if (dados.NotasDe10() > disponivel){
                caixa.setNotasDe10(caixa.getNotasDe10() + disponivel);
            }
            else{
                caixa.setNotasDe10(caixa.getNotasDe10() + dados.NotasDe10());
            }
        } else{
            throw new ValidacaoException("O número máximo de notas armazenadas neste caixa " +
                    "eletrônico é de 100 notas.\n Já estão armazenadas " + caixa.getNotasDe10() + " notas e " +
                    "você está tentando depositar " + dados.NotasDe10());
        }
        if (dados.NotasDe20() != null && (100 - caixa.getNotasDe20()) > 0) {
            int disponivel = 100 - caixa.getNotasDe20();
            if (dados.NotasDe20() > disponivel){
                caixa.setNotasDe20(caixa.getNotasDe20() + disponivel);
            }
            else{
                caixa.setNotasDe20(caixa.getNotasDe20() + dados.NotasDe20());
            }
        } else{
            throw new ValidacaoException("O número máximo de notas armazenadas neste caixa " +
                    "eletrônico é de 100 notas.\n Já estão armazenadas " + caixa.getNotasDe20() + " notas e " +
                    "você está tentando depositar " + dados.NotasDe20());
        }
        if (dados.NotasDe50() != null && dados.NotasDe20() != null && (100 - caixa.getNotasDe50()) > 0) {
            int disponivel = 100 - caixa.getNotasDe50();
            if (dados.NotasDe50() > disponivel){
                caixa.setNotasDe50(caixa.getNotasDe50() + disponivel);
            }
            else{
                caixa.setNotasDe50(caixa.getNotasDe50() + dados.NotasDe50());
            }
        } else{
            throw new ValidacaoException("O número máximo de notas armazenadas neste caixa " +
                    "eletrônico é de 100 notas.\n Já estão armazenadas " + caixa.getNotasDe50() + " notas e " +
                    "você está tentando depositar " + dados.NotasDe50());
        }
        if (dados.NotasDe100() != null && (100 - caixa.getNotasDe100()) > 0) {
            int disponivel = 100 - caixa.getNotasDe100();
            if (dados.NotasDe100() > disponivel){
                caixa.setNotasDe100(caixa.getNotasDe50() + dados.NotasDe50());
            }
            else{
                caixa.setNotasDe100(caixa.getNotasDe100() + dados.NotasDe100());
            }
        } else{
            throw new ValidacaoException("O número máximo de notas armazenadas neste caixa " +
                    "eletrônico é de 100 notas.\n Já estão armazenadas " + caixa.getNotasDe100() + " " +
                    "notas e" +
                    " " +
                    "você está tentando depositar " + dados.NotasDe100());
        }

        caixa.calcularSaldo();
        return caixa;
    }

    public DadosDetalhamentoSaque sacar(DadosSaque dados) {
        Cliente cliente = clienteRepository.getReferenceById(dados.idCliente());
        CaixaEletronico caixa = repository.getReferenceById(dados.idCaixa());
        Map<String,Integer> mapNotas = sacar(dados.valor(),caixa,cliente);
        Integer notasde100Entregues = mapNotas.get("notasDe100Entregues");
        Integer notasde50Entregues = mapNotas.get("notasDe50Entregues");
        Integer notasde20Entregues = mapNotas.get("notasDe20Entregues");
        Integer notasde10Entregues = mapNotas.get("notasDe10Entregues");
        if (notasde100Entregues == null){
            notasde100Entregues = 0;
        }
        if (notasde50Entregues == null){
            notasde50Entregues = 0;
        }if (notasde20Entregues == null){
            notasde20Entregues = 0;
        }if (notasde10Entregues == null){
            notasde10Entregues = 0;
        }
        return new DadosDetalhamentoSaque(cliente.getConta().getSaldo(), dados.valor(),notasde100Entregues,notasde50Entregues,notasde20Entregues,notasde10Entregues);
    }

    public Map<String,Integer> sacar(Double valor,CaixaEletronico caixa,Cliente cliente) {
        if (cliente.getConta().getSaldo() >= valor){
            if (valor <= caixa.getSaldoTotal()) {
                if (valor % 10 ==0){
                    Map<String,Integer> mapNotas = new HashMap<>();
                    mapNotas = saque100(caixa,valor,mapNotas);
                    caixa.calcularSaldo();
                    cliente.getConta().sacar(valor);
                    return mapNotas;
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


    public Map<String,Integer> saque100(CaixaEletronico caixa,double valor, Map mapNotas) {
        int qtdNotasNecessarias = (int) (valor / 100);
        Integer qtdNotasAntesDoSaque = caixa.getNotasDe100();
        if (caixa.getNotasDe100() > 0 && valor % 100 == 0) {
            if (qtdNotasNecessarias <= caixa.getNotasDe100()){
                caixa.setNotasDe100(caixa.getNotasDe100() - qtdNotasNecessarias);
                caixa.calcularSaldo();
                Integer notasDe100Entregues = qtdNotasAntesDoSaque - caixa.getNotasDe100();
                mapNotas.put("notasDe100Entregues",notasDe100Entregues);
                return mapNotas;
            }
            else {
                valor -= (caixa.getNotasDe100() * 100);
                caixa.calcularSaldo();
                caixa.setNotasDe100(0);
                Integer notasDe100Entregues = qtdNotasAntesDoSaque - caixa.getNotasDe100();
                mapNotas.put("notasDe100Entregues",notasDe100Entregues);
                saque50(valor,caixa,mapNotas);
                return mapNotas;
            }
        }else if(valor > 100 && caixa.getNotasDe100() > 0 && (caixa.getNotasDe100() <= (valor / 100))) {
            valor -= (caixa.getNotasDe100() * 100);
            caixa.calcularSaldo();
            caixa.setNotasDe100(0);
            Integer notasDe100Entregues = qtdNotasAntesDoSaque - caixa.getNotasDe100();
            mapNotas.put("notasDe100Entregues",notasDe100Entregues);
            saque50(valor,caixa,mapNotas);
            return mapNotas;
        }else if(valor > 100 && caixa.getNotasDe100() > 0 && (caixa.getNotasDe100() >= (valor / 100))){
            caixa.setNotasDe100(caixa.getNotasDe100() - qtdNotasNecessarias);
            valor -= (qtdNotasNecessarias * 100);
            caixa.calcularSaldo();
            Integer notasDe100Entregues = qtdNotasAntesDoSaque - caixa.getNotasDe100();
            mapNotas.put("notasDe100Entregues",notasDe100Entregues);
            saque50(valor,caixa,mapNotas);
            return mapNotas;
        }
        else{
            mapNotas.put("notasDe100Entregues",0);
            saque50(valor,caixa,mapNotas);
            return mapNotas;
        }
    }

    private Map<String,Integer> saque50(double valor,CaixaEletronico caixa,Map mapNotas) {
        int qtdNotasNecessarias = (int) (valor / 50);
        Integer qtdNotasAntesDoSaque = caixa.getNotasDe50();
        if (caixa.getNotasDe50() > 0 && valor % 50 == 0 && valor > 0) {
            if (qtdNotasNecessarias <= caixa.getNotasDe50()) {
                caixa.setNotasDe50(caixa.getNotasDe50() - qtdNotasNecessarias);
                caixa.calcularSaldo();
                Integer notasDe50Entregues = qtdNotasAntesDoSaque - caixa.getNotasDe50();
                mapNotas.put("notasDe50Entregues",notasDe50Entregues);
                return mapNotas;
            } else {
                valor -= (caixa.getNotasDe50() * 50);
                caixa.calcularSaldo();
                caixa.setNotasDe50(0);
                Integer notasDe50Entregues = qtdNotasAntesDoSaque - caixa.getNotasDe50();
                mapNotas.put("notasDe50Entregues",notasDe50Entregues);
                saque20(valor,caixa,mapNotas);
                return mapNotas;
            }

        }else if(valor > 50 && caixa.getNotasDe50() > 0 && (caixa.getNotasDe50() <= (valor / 50))) {
            valor -= (caixa.getNotasDe50() * 50);
            caixa.calcularSaldo();
            caixa.setNotasDe50(0);
            Integer notasDe50Entregues = qtdNotasAntesDoSaque - qtdNotasNecessarias;
            mapNotas.put("notasDe50Entregues",notasDe50Entregues);
            saque20(valor,caixa,mapNotas);
            return mapNotas;
        }else if(valor > 50 && caixa.getNotasDe50() > 0 && (caixa.getNotasDe50() >= (valor / 50))){
            caixa.setNotasDe50(caixa.getNotasDe50()- qtdNotasNecessarias);
            valor -= (qtdNotasNecessarias * 50);
            caixa.calcularSaldo();
            Integer notasDe50Entregues = qtdNotasAntesDoSaque - caixa.getNotasDe50();
            mapNotas.put("notasDe50Entregues",notasDe50Entregues);
            saque20(valor,caixa,mapNotas);
            return mapNotas;
        }
        else {
            mapNotas.put("notasDe50Entregues",0);
            saque20(valor,caixa,mapNotas);
            return mapNotas;
        }
    }

    private Map<String,Integer> saque20(double valor, CaixaEletronico caixa,Map mapNotas) {
        int qtdNotasNecessarias = (int) (valor / 20);
        Integer qtdNotasAntesDoSaque = caixa.getNotasDe20();
        if (caixa.getNotasDe20() > 0 && valor % 20 == 0 && valor > 0) {
            if (qtdNotasNecessarias <= caixa.getNotasDe20()) {
                caixa.setNotasDe20(caixa.getNotasDe20()- qtdNotasNecessarias);
                caixa.calcularSaldo();
                Integer notasDe20Entregues = qtdNotasAntesDoSaque - caixa.getNotasDe20();
                mapNotas.put("notasDe20Entregues",notasDe20Entregues);
                return mapNotas;
            } else {
                valor -= (caixa.getNotasDe20() * 20);
                caixa.calcularSaldo();
                caixa.setNotasDe20(0);
                Integer notasDe20Entregues = qtdNotasAntesDoSaque - caixa.getNotasDe20();
                mapNotas.put("notasDe20Entregues",notasDe20Entregues);
                saque10(valor,caixa,mapNotas);
                return mapNotas;
            }
        }else if(valor > 20 && caixa.getNotasDe20() > 0 && (caixa.getNotasDe20() <= (valor / 20))) {
            valor -= (caixa.getNotasDe20() * 20);
            caixa.calcularSaldo();
            caixa.setNotasDe20(0);
            Integer notasDe20Entregues = qtdNotasAntesDoSaque - qtdNotasNecessarias;
            mapNotas.put("notasDe20Entregues",notasDe20Entregues);
            saque10(valor,caixa,mapNotas);
            return mapNotas;
        }else if(valor > 20 && caixa.getNotasDe20() > 0 && (caixa.getNotasDe20() >= (valor / 20))){
            caixa.setNotasDe20(caixa.getNotasDe20()- qtdNotasNecessarias);
            valor -= (qtdNotasNecessarias * 20);
            caixa.calcularSaldo();
            Integer notasDe20Entregues = qtdNotasAntesDoSaque - caixa.getNotasDe20();
            mapNotas.put("notasDe20Entregues",notasDe20Entregues);
            saque10(valor,caixa,mapNotas);
            return mapNotas;
        }
        else {
            mapNotas.put("notasDe20Entregues",0);
            saque10(valor,caixa,mapNotas);
            return mapNotas;
        }
    }

    private Map<String,Integer> saque10(double valor,CaixaEletronico caixa,Map mapNotas) {
        int qtdNotasNecessarias = (int) (valor / 10);
        Integer qtdNotasAntesDoSaque = caixa.getNotasDe10();
        if (caixa.getNotasDe10() > 0 && valor % 10 == 0 && valor > 0) {
            if (qtdNotasNecessarias <= caixa.getNotasDe10()){
                caixa.setNotasDe10(caixa.getNotasDe10()- qtdNotasNecessarias);
                caixa.calcularSaldo();
                Integer notasDe10Entregues = qtdNotasAntesDoSaque - caixa.getNotasDe10();
                mapNotas.put("notasDe10Entregues",notasDe10Entregues);
                return mapNotas;
            }else {
                caixa.setNotasDe10(0);
                caixa.calcularSaldo();
                Integer notasDe10Entregues = qtdNotasAntesDoSaque - caixa.getNotasDe10();
                mapNotas.put("notasDe10Entregues",notasDe10Entregues);
                return mapNotas;
            }
        }
        mapNotas.put("notasDe10Entregues",0);
        return mapNotas;
    }

    public Cliente depositar(DadosDeposito dados) {
        Cliente cliente = clienteRepository.getReferenceById(dados.idCliente());
        cliente.getConta().depositar(dados.valor());
        return cliente;
    }
}
