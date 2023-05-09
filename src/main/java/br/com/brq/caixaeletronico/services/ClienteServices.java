package br.com.brq.caixaeletronico.services;

import br.com.brq.caixaeletronico.controller.request.DadosCadastroCaixa;
import br.com.brq.caixaeletronico.controller.request.DadosClienteCadastro;
import br.com.brq.caixaeletronico.controller.response.DadosListagemCliente;
import br.com.brq.caixaeletronico.model.CaixaEletronico;
import br.com.brq.caixaeletronico.model.Cliente;
import br.com.brq.caixaeletronico.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClienteServices {
    @Autowired
    private ClienteRepository repository;


    public Cliente cadastrarCliente(DadosClienteCadastro dados) {
        Cliente cliente = new Cliente(dados);
        repository.save(cliente);
        return cliente;
    }

    public Page listarClientes(Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemCliente::new);
    }

}
