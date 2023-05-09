package br.com.brq.caixaeletronico.controller;

import br.com.brq.caixaeletronico.controller.request.DadosClienteCadastro;
import br.com.brq.caixaeletronico.controller.request.DadosDeposito;
import br.com.brq.caixaeletronico.controller.request.DadosSaque;
import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoCliente;
import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoSaque;
import br.com.brq.caixaeletronico.controller.response.DadosListagemCliente;
import br.com.brq.caixaeletronico.model.CaixaEletronico;
import br.com.brq.caixaeletronico.model.Cliente;
import br.com.brq.caixaeletronico.repository.CaixaEletronicoRepository;
import br.com.brq.caixaeletronico.repository.ClienteRepository;
import br.com.brq.caixaeletronico.services.ClienteServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteServices services;

    @PostMapping("/cadastro")
    public ResponseEntity cadastrarCliente(@RequestBody @Valid DadosClienteCadastro dados,
                                           UriComponentsBuilder uriBuilder) {
    Cliente cliente = services.cadastrarCliente(dados);
    var uri = uriBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();
    return ResponseEntity.created(uri).body(new DadosDetalhamentoCliente(cliente));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemCliente>> listarClientes(@PageableDefault(size = 10,
            sort = {"nome"})Pageable paginacao) {
        return ResponseEntity.ok(services.listarClientes(paginacao));
    }

}
