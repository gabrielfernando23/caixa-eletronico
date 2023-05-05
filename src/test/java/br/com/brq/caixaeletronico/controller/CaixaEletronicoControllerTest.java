package br.com.brq.caixaeletronico.controller;

import br.com.brq.caixaeletronico.ValidacaoException;
import br.com.brq.caixaeletronico.caixa.DadosAtualizarNotas;
import br.com.brq.caixaeletronico.caixa.DadosCadastroCaixa;
import br.com.brq.caixaeletronico.caixa.DadosDetalhamentoCaixa;
import br.com.brq.caixaeletronico.cliente.DadosClienteCadastro;
import br.com.brq.caixaeletronico.infra.exception.TratadorDeErros;
import io.github.glytching.junit.extension.exception.ExpectedException;
import net.bytebuddy.asm.Advice;
import org.apache.coyote.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
class CaixaEletronicoControllerTest {

    @Test
    @DisplayName("Deveria devolver código http 201 quando todas as informações forem preenchidas " +
            "corretamente")
    void cadastrarCaixa() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DadosCadastroCaixa dadosCadastroCaixa = new DadosCadastroCaixa(20,10,10,10);
        HttpEntity<DadosCadastroCaixa> request = new HttpEntity<>(dadosCadastroCaixa, headers);

        ResponseEntity<DadosDetalhamentoCaixa> response = restTemplate.exchange(
                "http://localhost:8080/caixa/cadastrar",
                HttpMethod.POST,
                request,
                DadosDetalhamentoCaixa.class
        );

        assertEquals(HttpStatus.CREATED.value(),response.getStatusCode().value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 quando todos os dados forem inseridos " +
            "corretamente e o caixa estiver com menos de 100 notas")
    void atualizarNotas() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DadosCadastroCaixa dadosCadastroCaixa = new DadosCadastroCaixa(20,10,10,10);
        HttpEntity<DadosCadastroCaixa> request = new HttpEntity<>(dadosCadastroCaixa, headers);

        ResponseEntity<DadosDetalhamentoCaixa> response = restTemplate.exchange(
                "http://localhost:8080/caixa/cadastrar",
                HttpMethod.POST,
                request,
                DadosDetalhamentoCaixa.class
        );

        DadosAtualizarNotas dadosAtualizarNotas = new DadosAtualizarNotas(response.getBody().id(),5,5
                ,5,5);
        HttpEntity<DadosAtualizarNotas> requestAtualizacao = new HttpEntity<>(dadosAtualizarNotas);

        ResponseEntity<DadosAtualizarNotas> responseAtualizacao = restTemplate.exchange(
                "http://localhost:8080/caixa",
                HttpMethod.PUT,
                requestAtualizacao,
                DadosAtualizarNotas.class
        );

        assertEquals(HttpStatus.OK.value(),responseAtualizacao.getStatusCode().value());
    }


    @Test
    @DisplayName("Deveria devolver código http ")
    void atualizarNotasCenario02() throws ValidacaoException{
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DadosCadastroCaixa dadosCadastroCaixa = new DadosCadastroCaixa(100,100,100,100);
        HttpEntity<DadosCadastroCaixa> request = new HttpEntity<>(dadosCadastroCaixa, headers);

        ResponseEntity<DadosDetalhamentoCaixa> response = restTemplate.exchange(
                "http://localhost:8080/caixa/cadastrar",
                HttpMethod.POST,
                request,
                DadosDetalhamentoCaixa.class
        );
            DadosAtualizarNotas dadosAtualizarNotas = new DadosAtualizarNotas(response.getBody().id(),5,5
                    ,5,5);
            HttpEntity<DadosAtualizarNotas> requestAtualizacao = new HttpEntity<>(dadosAtualizarNotas,headers);
            try{
                ResponseEntity<DadosDetalhamentoCaixa> responseAtualizacao = restTemplate.exchange("http://localhost" +
                                ":8080" +
                                "/caixa",
                        HttpMethod.PUT,requestAtualizacao,DadosDetalhamentoCaixa.class);
            }catch (Exception e){
                assertEquals("400 : \"O número máximo de notas armazenadas neste caixa eletrônico é de 100 notas.<EOL> Já estão armazenadas 100 notas e você está tentando depositar 5\"",e.getMessage());
            }

    }
}