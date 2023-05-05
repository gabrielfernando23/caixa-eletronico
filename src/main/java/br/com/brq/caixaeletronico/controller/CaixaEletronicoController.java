package br.com.brq.caixaeletronico.controller;

import br.com.brq.caixaeletronico.caixa.CaixaEletronicoRepository;
import br.com.brq.caixaeletronico.caixa.DadosAtualizarNotas;
import br.com.brq.caixaeletronico.caixa.DadosCadastroCaixa;
import br.com.brq.caixaeletronico.caixa.DadosDetalhamentoCaixa;
import br.com.brq.caixaeletronico.model.CaixaEletronico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/caixa")
public class CaixaEletronicoController {
    @Autowired
    CaixaEletronicoRepository repository;

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrarCaixa(@RequestBody DadosCadastroCaixa dados,
                                         UriComponentsBuilder uriBuilder) {
        CaixaEletronico caixa = new CaixaEletronico(dados.NotasDe100(),
                dados.NotasDe50(),dados.NotasDe20(),dados.NotasDe10());
        repository.save(caixa);
        var uri = uriBuilder.path("/caixa/{id}").buildAndExpand(caixa.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoCaixa(caixa));
    }

    @GetMapping("/notas/{id}")
    public ResponseEntity mostrarNotas(@PathVariable Long id) {
        var caixa = repository.findById(id).map(DadosDetalhamentoCaixa::new);
        return ResponseEntity.ok(caixa);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarNotas(@RequestBody DadosAtualizarNotas dados) {
    CaixaEletronico caixa = repository.getReferenceById(dados.id());
    caixa.atualizarInformacoes(dados);
    return ResponseEntity.ok(new DadosDetalhamentoCaixa(caixa));
    }

}
