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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        }if (notasde50Entregues == null){
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
                    List<Integer> notas = new ArrayList<>(4);
                    notas.add(100);
                    notas.add(50);
                    notas.add(20);
                    notas.add(10);
                    Map<String,Integer> mapNotas = saque(caixa,valor,notas);
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

    private Map<String, Integer> saque(CaixaEletronico caixa, Double valor, List<Integer> notas) {
        Map<String,Integer> mapNotas = new HashMap<>();
        Double valorSaque = valor;
        for (int i = 0; i < notas.size(); i++){
            if (valorSaque > 9){
                int qtdNotasNecessarias = (int) (valorSaque / notas.get(i));
                Integer qtdNotasAntesDoSaque = verificarGet(caixa,notas.get(i));
                if (verificarGet(caixa,notas.get(i)) > 0 && valorSaque % notas.get(i) == 0) {
                    if (qtdNotasNecessarias <= verificarGet(caixa,notas.get(i))){
                        Integer qtdNotas = (verificarGet(caixa,notas.get(i)) - qtdNotasNecessarias);
                        verificarSet(notas.get(i),caixa,qtdNotas);
                        caixa.calcularSaldo();
                        valorSaque -= (qtdNotasAntesDoSaque - verificarGet(caixa,notas.get(i))) * notas.get(i);
                        mapNotas.put("notasDe"+notas.get(i)+"Entregues",qtdNotasAntesDoSaque - verificarGet(caixa,notas.get(i)));
                    }
                    else {
                        verificarSet(notas.get(i),caixa,0);
                        valorSaque -= (qtdNotasAntesDoSaque - verificarGet(caixa,notas.get(i))) * notas.get(i);
                        mapNotas.put("notasDe"+notas.get(i)+"Entregues",qtdNotasAntesDoSaque - verificarGet(caixa,notas.get(i)));
                    }
                }else if(valorSaque > notas.get(i) && verificarGet(caixa,notas.get(i)) > 0 && (verificarGet(caixa,notas.get(i)) <= qtdNotasNecessarias)) {
                    verificarSet(notas.get(i),caixa,0);
                    valorSaque -= (qtdNotasNecessarias * notas.get(i));
                    mapNotas.put("notasDe"+notas.get(i)+"Entregues",qtdNotasAntesDoSaque - verificarGet(caixa,notas.get(i)));
                }else if(valorSaque > notas.get(i) && verificarGet(caixa,notas.get(i)) > 0 && (verificarGet(caixa,notas.get(i)) >= qtdNotasNecessarias)){
                    Integer qtdNotas = (verificarGet(caixa,notas.get(i)) - qtdNotasNecessarias);
                    verificarSet(notas.get(i),caixa,qtdNotas);
                    valorSaque -= (qtdNotasNecessarias * notas.get(i));
                    caixa.calcularSaldo();
                    mapNotas.put("notasDe"+notas.get(i)+"Entregues",qtdNotasAntesDoSaque - verificarGet(caixa,notas.get(i)));
                }
                else{
                    mapNotas.put("notasDe"+notas.get(i)+"Entregues",0);
                }
            }
        }
        return mapNotas;
    }

    private void verificarSet(Integer nota,CaixaEletronico caixa, Integer qtdNotas) {
        if (nota == 100){
            caixa.setNotasDe100(qtdNotas);
        }if (nota == 50){
            caixa.setNotasDe50(qtdNotas);
        }if (nota == 20){
            caixa.setNotasDe20(qtdNotas);
        }if (nota == 10){
            caixa.setNotasDe10(qtdNotas);
        }
    }

    private Integer verificarGet(CaixaEletronico caixa,Integer num) {
        if (num == 100){
            return caixa.getNotasDe100();
        }if (num == 50){
            return caixa.getNotasDe50();
        }if (num == 20){
            return caixa.getNotasDe20();
        }if (num == 10){
            return caixa.getNotasDe10();
        }
        return null;
    }
    public Cliente depositar(DadosDeposito dados) {
        Cliente cliente = clienteRepository.getReferenceById(dados.idCliente());
        cliente.getConta().depositar(dados.valor());
        return cliente;
    }
}
