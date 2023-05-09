package br.com.brq.caixaeletronico.controller;

import br.com.brq.caixaeletronico.controller.request.DadosDeposito;
import br.com.brq.caixaeletronico.controller.request.DadosSaque;
import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoCliente;
import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoSaque;
import br.com.brq.caixaeletronico.model.Cliente;
import br.com.brq.caixaeletronico.controller.request.DadosAtualizarNotas;
import br.com.brq.caixaeletronico.controller.request.DadosCadastroCaixa;
import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoCaixa;
import br.com.brq.caixaeletronico.model.CaixaEletronico;
import br.com.brq.caixaeletronico.services.CaixaEletronicoServices;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/caixa")
public class CaixaEletronicoController {
    @Autowired
    CaixaEletronicoServices services;

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrarCaixa(@RequestBody @Valid DadosCadastroCaixa dados,
                                         UriComponentsBuilder uriBuilder) {
        CaixaEletronico caixa = services.cadastrarCaixa(dados);
        var uri = uriBuilder.path("/caixa/{id}").buildAndExpand(caixa.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoCaixa(caixa));
    }

    @GetMapping("/notas/{id}")
    public ResponseEntity mostrarNotas(@PathVariable @Valid @NotNull Long id) {
        return ResponseEntity.ok(services.mostrarNotas(id));
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarNotas(@RequestBody @Valid DadosAtualizarNotas dados) {

    CaixaEletronico caixa = services.atualizarNotas(dados);
    return ResponseEntity.ok(new DadosDetalhamentoCaixa(caixa));
    }

    @PutMapping("/sacar")
    @Transactional
    public ResponseEntity sacar(@RequestBody @Valid DadosSaque dados) {
        DadosDetalhamentoSaque dadosSaque = services.sacar(dados);
        return ResponseEntity.ok(dadosSaque);
    }

    @PostMapping("/depositar")
    @Transactional
    public ResponseEntity depositar(@RequestBody @Valid DadosDeposito dados) {
        Cliente cliente = services.depositar(dados);
        return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
    }

}
