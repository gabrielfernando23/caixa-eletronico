package br.com.brq.caixaeletronico.controller;

import br.com.brq.caixaeletronico.controller.request.*;
import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoCaixa;
import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoCliente;
import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoSaque;
import br.com.brq.caixaeletronico.repository.ClienteRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.filters.ExpiresFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.ClassBasedNavigableIterableAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ClienteControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Deveria devolver código http 200 quando todas informações forem preenchidas " +
            "corretamente" +
            "tp 200 quando cadastrar cliente")
    void cadastrar() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DadosCriacaoConta dadosCriacaoConta = new DadosCriacaoConta("123456","0100",100.0);
        DadosClienteCadastro dadosClienteCadastro =
                new DadosClienteCadastro("Gabriel", "ogabrielsantos@brq.com","11999261921","12345",dadosCriacaoConta);
        HttpEntity<DadosClienteCadastro> request = new HttpEntity<>(dadosClienteCadastro, headers);

        ResponseEntity<DadosClienteCadastro> response = restTemplate.exchange(
                "http://localhost:8080/clientes/cadastro",
                HttpMethod.POST,
                request,
                DadosClienteCadastro.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

}