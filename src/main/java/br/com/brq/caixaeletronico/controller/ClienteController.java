package br.com.brq.caixaeletronico.controller;

import br.com.brq.caixaeletronico.caixa.CaixaEletronicoRepository;
import br.com.brq.caixaeletronico.cliente.*;
import br.com.brq.caixaeletronico.conta.DadosDetalhamentoConta;
import br.com.brq.caixaeletronico.model.CaixaEletronico;
import br.com.brq.caixaeletronico.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired
    private ClienteRepository repository;
    @Autowired
    private CaixaEletronicoRepository caixaEletronicoRepository;

    @PostMapping("/cadastro")
    public ResponseEntity cadastrarCliente(@RequestBody DadosClienteCadastro dados,
                                           UriComponentsBuilder uriBuilder) {
    Cliente cliente = new Cliente(dados);
    repository.save(cliente);

    var uri = uriBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();
    return ResponseEntity.created(uri).body(new DadosDetalhamentoCliente(cliente));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemCliente>> listarClientes(@PageableDefault(size = 10,
            sort = {"nome"})Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemCliente::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity sacar(@RequestBody DadosSaque dados) {
        Cliente cliente = repository.getReferenceById(dados.idCliente());
        CaixaEletronico caixa = caixaEletronicoRepository.getReferenceById(dados.idCaixa());
        if (caixa.sacar(dados.valor(),cliente)){
            cliente.getConta().sacar(dados.valor());
        }
        return ResponseEntity.ok(new DadosDetalhamentoSaque(cliente,caixa,dados.valor()));
    }

    @PostMapping("/depositar")
    @Transactional
    public ResponseEntity depositar(@RequestBody DadosDeposito dados) {
        Cliente cliente = repository.getReferenceById(dados.idCliente());
        cliente.getConta().depositar(dados.valor());
        return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
    }
}
