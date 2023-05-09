package br.com.brq.caixaeletronico.controller;

import br.com.brq.caixaeletronico.controller.request.*;
import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoCliente;
import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoSaque;
import br.com.brq.caixaeletronico.exception.ValidacaoException;
import br.com.brq.caixaeletronico.controller.response.DadosDetalhamentoCaixa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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


    @Test
    @DisplayName("Deve responder código http 200 quando todos os dados de depósito forem " +
            "inseridos corretamente e o idUsuário referenciar um usuário cadastrado no banco de " +
            "dados")
    void depositar() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DadosCriacaoConta dadosCriacaoConta = new DadosCriacaoConta("123456","0100",100.0);
        DadosClienteCadastro dadosClienteCadastro =
                new DadosClienteCadastro("Gabriel", "ogabrielsantos@brq.com","11999261921","12345",dadosCriacaoConta);
        HttpEntity<DadosClienteCadastro> request = new HttpEntity<>(dadosClienteCadastro, headers);

        ResponseEntity<DadosDetalhamentoCliente> response = restTemplate.exchange(
                "http://localhost:8080/clientes/cadastro",
                HttpMethod.POST,
                request,
                DadosDetalhamentoCliente.class
        );

        DadosDeposito dadosDeposito = new DadosDeposito(response.getBody().id(), 100.0);
        HttpEntity<DadosDeposito> requestDeposito = new HttpEntity<>(dadosDeposito, headers);

        ResponseEntity<DadosDetalhamentoCliente> responseDeposito = restTemplate.exchange(
                "http://localhost:8080/caixa/depositar",
                HttpMethod.POST,
                requestDeposito,
                DadosDetalhamentoCliente.class
        );

        assertEquals(HttpStatus.OK.value(),responseDeposito.getStatusCode().value());
    }


    @Test
    @DisplayName("Deveria devolver código http 200 caso o usuário tenha saldo suficiente, o " +
            "caixa tenha notas disponíveis e todos os dados tenham sido preenchidos corretamente")
    void sacar() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DadosCriacaoConta dadosCriacaoConta = new DadosCriacaoConta("123456","0100",100.0);
        DadosClienteCadastro dadosClienteCadastro =
                new DadosClienteCadastro("Gabriel", "ogabrielsantos@brq.com","11999261921","12345",dadosCriacaoConta);
        HttpEntity<DadosClienteCadastro> request = new HttpEntity<>(dadosClienteCadastro, headers);

        ResponseEntity<DadosDetalhamentoCliente> response = restTemplate.exchange(
                "http://localhost:8080/clientes/cadastro",
                HttpMethod.POST,
                request,
                DadosDetalhamentoCliente.class
        );

        DadosCadastroCaixa dadosCadastroCaixa = new DadosCadastroCaixa(100,100,100,100);
        HttpEntity<DadosCadastroCaixa> requestCadastroCaixa = new HttpEntity<>(dadosCadastroCaixa,
                headers);

        ResponseEntity<DadosDetalhamentoCaixa> responseCadastroCaixa = restTemplate.exchange(
                "http://localhost:8080/caixa/cadastrar",
                HttpMethod.POST,
                requestCadastroCaixa,
                DadosDetalhamentoCaixa.class
        );

        DadosSaque dadosSaque = new DadosSaque(response.getBody().id(),
                responseCadastroCaixa.getBody().id(), 100.0);
        HttpEntity<DadosSaque> requestSaque = new HttpEntity<>(dadosSaque,headers);

        ResponseEntity<DadosDetalhamentoSaque> responseSaque = restTemplate.exchange(
                "http://localhost:8080/caixa/sacar",
                HttpMethod.PUT,
                requestSaque,
                DadosDetalhamentoSaque.class
        );

        assertEquals(HttpStatus.OK,responseSaque.getStatusCode());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o menor número de notas possíveis")
    void sacarCenario02() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DadosCriacaoConta dadosCriacaoConta = new DadosCriacaoConta("123456","0100",150.0);
        DadosClienteCadastro dadosClienteCadastro =
                new DadosClienteCadastro("Gabriel", "ogabrielsantos@brq.com","11999261921","12345",dadosCriacaoConta);
        HttpEntity<DadosClienteCadastro> request = new HttpEntity<>(dadosClienteCadastro, headers);

        ResponseEntity<DadosDetalhamentoCliente> response = restTemplate.exchange(
                "http://localhost:8080/clientes/cadastro",
                HttpMethod.POST,
                request,
                DadosDetalhamentoCliente.class
        );

        DadosCadastroCaixa dadosCadastroCaixa = new DadosCadastroCaixa(1,3,0,0);
        HttpEntity<DadosCadastroCaixa> requestCadastroCaixa = new HttpEntity<>(dadosCadastroCaixa,
                headers);

        ResponseEntity<DadosDetalhamentoCaixa> responseCadastroCaixa = restTemplate.exchange(
                "http://localhost:8080/caixa/cadastrar",
                HttpMethod.POST,
                requestCadastroCaixa,
                DadosDetalhamentoCaixa.class
        );

        DadosSaque dadosSaque = new DadosSaque(response.getBody().id(),
                responseCadastroCaixa.getBody().id(), 150.0);
        HttpEntity<DadosSaque> requestSaque = new HttpEntity<>(dadosSaque,headers);

        ResponseEntity<DadosDetalhamentoSaque> responseSaque = restTemplate.exchange(
                "http://localhost:8080/caixa/sacar",
                HttpMethod.PUT,
                requestSaque,
                DadosDetalhamentoSaque.class
        );

        Long id = responseCadastroCaixa.getBody().id();

        ResponseEntity<DadosDetalhamentoCaixa> responseListagemCaixa = restTemplate.exchange(
                "http" +
                        "://localhost:8080/caixa/notas/" + id, HttpMethod.GET,null,
                DadosDetalhamentoCaixa.class);

        assertEquals(HttpStatus.OK,responseSaque.getStatusCode());
        assertEquals(150.0,responseSaque.getBody().ValorSaque());
        assertEquals(0,responseListagemCaixa.getBody().NotasDe100());
        assertEquals(2,responseListagemCaixa.getBody().NotasDe50());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o menor número de notas possíveis")
    void sacarCenario03() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DadosCriacaoConta dadosCriacaoConta = new DadosCriacaoConta("123456","0100",200.0);
        DadosClienteCadastro dadosClienteCadastro =
                new DadosClienteCadastro("Gabriel", "ogabrielsantos@brq.com","11999261921","12345",dadosCriacaoConta);
        HttpEntity<DadosClienteCadastro> request = new HttpEntity<>(dadosClienteCadastro, headers);

        ResponseEntity<DadosDetalhamentoCliente> response = restTemplate.exchange(
                "http://localhost:8080/clientes/cadastro",
                HttpMethod.POST,
                request,
                DadosDetalhamentoCliente.class
        );

        DadosCadastroCaixa dadosCadastroCaixa = new DadosCadastroCaixa(2,2,1,3);
        HttpEntity<DadosCadastroCaixa> requestCadastroCaixa = new HttpEntity<>(dadosCadastroCaixa,
                headers);

        ResponseEntity<DadosDetalhamentoCaixa> responseCadastroCaixa = restTemplate.exchange(
                "http://localhost:8080/caixa/cadastrar",
                HttpMethod.POST,
                requestCadastroCaixa,
                DadosDetalhamentoCaixa.class
        );

        DadosSaque dadosSaque = new DadosSaque(response.getBody().id(),
                responseCadastroCaixa.getBody().id(), 190.0);
        HttpEntity<DadosSaque> requestSaque = new HttpEntity<>(dadosSaque,headers);

        ResponseEntity<DadosDetalhamentoSaque> responseSaque = restTemplate.exchange(
                "http://localhost:8080/caixa/sacar",
                HttpMethod.PUT,
                requestSaque,
                DadosDetalhamentoSaque.class
        );

        Long id = responseCadastroCaixa.getBody().id();

        ResponseEntity<DadosDetalhamentoCaixa> responseListagemCaixa = restTemplate.exchange(
                "http" +
                        "://localhost:8080/caixa/notas/" + id, HttpMethod.GET,null,
                DadosDetalhamentoCaixa.class);

        assertEquals(HttpStatus.OK,responseSaque.getStatusCode());
        assertEquals(190.0,responseSaque.getBody().ValorSaque());
        assertEquals(1,responseListagemCaixa.getBody().NotasDe100());
        assertEquals(1,responseListagemCaixa.getBody().NotasDe50());
        assertEquals(0,responseListagemCaixa.getBody().NotasDe20());
        assertEquals(1,responseListagemCaixa.getBody().NotasDe10());
    }

    @Test
    @DisplayName("Deveria devolver o saldo do cliente após efetuar o saque")
    void sacarCenario04() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DadosCriacaoConta dadosCriacaoConta = new DadosCriacaoConta("123456","0100",200.0);
        DadosClienteCadastro dadosClienteCadastro =
                new DadosClienteCadastro("Gabriel", "ogabrielsantos@brq.com","11999261921","12345",dadosCriacaoConta);
        HttpEntity<DadosClienteCadastro> request = new HttpEntity<>(dadosClienteCadastro, headers);

        ResponseEntity<DadosDetalhamentoCliente> response = restTemplate.exchange(
                "http://localhost:8080/clientes/cadastro",
                HttpMethod.POST,
                request,
                DadosDetalhamentoCliente.class
        );

        DadosCadastroCaixa dadosCadastroCaixa = new DadosCadastroCaixa(2,2,1,3);
        HttpEntity<DadosCadastroCaixa> requestCadastroCaixa = new HttpEntity<>(dadosCadastroCaixa,
                headers);

        ResponseEntity<DadosDetalhamentoCaixa> responseCadastroCaixa = restTemplate.exchange(
                "http://localhost:8080/caixa/cadastrar",
                HttpMethod.POST,
                requestCadastroCaixa,
                DadosDetalhamentoCaixa.class
        );

        DadosSaque dadosSaque = new DadosSaque(response.getBody().id(),
                responseCadastroCaixa.getBody().id(), 150.0);
        HttpEntity<DadosSaque> requestSaque = new HttpEntity<>(dadosSaque,headers);

        ResponseEntity<DadosDetalhamentoSaque> responseSaque = restTemplate.exchange(
                "http://localhost:8080/caixa/sacar",
                HttpMethod.PUT,
                requestSaque,
                DadosDetalhamentoSaque.class
        );

        Long id = responseCadastroCaixa.getBody().id();

        ResponseEntity<DadosDetalhamentoCaixa> responseListagemCaixa = restTemplate.exchange(
                "http" +
                        "://localhost:8080/caixa/notas/" + id, HttpMethod.GET,null,
                DadosDetalhamentoCaixa.class);

        assertEquals(HttpStatus.OK,responseSaque.getStatusCode());
        assertEquals(50.0,responseSaque.getBody().saldoAtual());
    }


    @Test
    @DisplayName("Deveria lançar exceção caso o cliente não tenha saldo")
    void sacarCenario05() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DadosCriacaoConta dadosCriacaoConta = new DadosCriacaoConta("123456","0100",0.0);
        DadosClienteCadastro dadosClienteCadastro =
                new DadosClienteCadastro("Gabriel", "ogabrielsantos@brq.com","11999261921","12345",dadosCriacaoConta);
        HttpEntity<DadosClienteCadastro> request = new HttpEntity<>(dadosClienteCadastro, headers);

        ResponseEntity<DadosDetalhamentoCliente> response = restTemplate.exchange(
                "http://localhost:8080/clientes/cadastro",
                HttpMethod.POST,
                request,
                DadosDetalhamentoCliente.class
        );

        DadosCadastroCaixa dadosCadastroCaixa = new DadosCadastroCaixa(2,2,1,3);
        HttpEntity<DadosCadastroCaixa> requestCadastroCaixa = new HttpEntity<>(dadosCadastroCaixa,
                headers);

        ResponseEntity<DadosDetalhamentoCaixa> responseCadastroCaixa = restTemplate.exchange(
                "http://localhost:8080/caixa/cadastrar",
                HttpMethod.POST,
                requestCadastroCaixa,
                DadosDetalhamentoCaixa.class
        );

        DadosSaque dadosSaque = new DadosSaque(response.getBody().id(),
                responseCadastroCaixa.getBody().id(), 150.0);
        HttpEntity<DadosSaque> requestSaque = new HttpEntity<>(dadosSaque,headers);
        try{
            ResponseEntity<DadosDetalhamentoSaque> responseSaque = restTemplate.exchange(
                    "http://localhost:8080/caixa/sacar",
                    HttpMethod.PUT,
                    requestSaque,
                    DadosDetalhamentoSaque.class
            );
        } catch (Exception e){
            assertEquals("400 : \"Saldo insuficiente\"",e.getMessage());
        }
    }

    @Test
    @DisplayName("Deveria lançar exceção caso o caixa não tenha notas suficientes")
    void sacarCenario06() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DadosCriacaoConta dadosCriacaoConta = new DadosCriacaoConta("123456","0100",200.0);
        DadosClienteCadastro dadosClienteCadastro =
                new DadosClienteCadastro("Gabriel", "ogabrielsantos@brq.com","11999261921","12345",dadosCriacaoConta);
        HttpEntity<DadosClienteCadastro> request = new HttpEntity<>(dadosClienteCadastro, headers);

        ResponseEntity<DadosDetalhamentoCliente> response = restTemplate.exchange(
                "http://localhost:8080/clientes/cadastro",
                HttpMethod.POST,
                request,
                DadosDetalhamentoCliente.class
        );

        DadosCadastroCaixa dadosCadastroCaixa = new DadosCadastroCaixa(0,0,0,0);
        HttpEntity<DadosCadastroCaixa> requestCadastroCaixa = new HttpEntity<>(dadosCadastroCaixa,
                headers);

        ResponseEntity<DadosDetalhamentoCaixa> responseCadastroCaixa = restTemplate.exchange(
                "http://localhost:8080/caixa/cadastrar",
                HttpMethod.POST,
                requestCadastroCaixa,
                DadosDetalhamentoCaixa.class
        );

        DadosSaque dadosSaque = new DadosSaque(response.getBody().id(),
                responseCadastroCaixa.getBody().id(), 150.0);
        HttpEntity<DadosSaque> requestSaque = new HttpEntity<>(dadosSaque,headers);
        try{
            ResponseEntity<DadosDetalhamentoSaque> responseSaque = restTemplate.exchange(
                    "http://localhost:8080/caixa/sacar",
                    HttpMethod.PUT,
                    requestSaque,
                    DadosDetalhamentoSaque.class
            );
        } catch (Exception e){
            assertEquals("400 : \"Não há notas disponíveis para realizar este saque\"",e.getMessage());
        }
    }

    @Test
    @DisplayName("Deveria lançar exceção caso o caixa não tenha notas suficientes")
    void sacarCenario07() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DadosCriacaoConta dadosCriacaoConta = new DadosCriacaoConta("123456","0100",200.0);
        DadosClienteCadastro dadosClienteCadastro =
                new DadosClienteCadastro("Gabriel", "ogabrielsantos@brq.com","11999261921","12345",dadosCriacaoConta);
        HttpEntity<DadosClienteCadastro> request = new HttpEntity<>(dadosClienteCadastro, headers);

        ResponseEntity<DadosDetalhamentoCliente> response = restTemplate.exchange(
                "http://localhost:8080/clientes/cadastro",
                HttpMethod.POST,
                request,
                DadosDetalhamentoCliente.class
        );

        DadosCadastroCaixa dadosCadastroCaixa = new DadosCadastroCaixa(1,0,2,0);
        HttpEntity<DadosCadastroCaixa> requestCadastroCaixa = new HttpEntity<>(dadosCadastroCaixa,
                headers);

        ResponseEntity<DadosDetalhamentoCaixa> responseCadastroCaixa = restTemplate.exchange(
                "http://localhost:8080/caixa/cadastrar",
                HttpMethod.POST,
                requestCadastroCaixa,
                DadosDetalhamentoCaixa.class
        );

        DadosSaque dadosSaque = new DadosSaque(response.getBody().id(),
                responseCadastroCaixa.getBody().id(), 150.0);
        HttpEntity<DadosSaque> requestSaque = new HttpEntity<>(dadosSaque,headers);
        try{
            ResponseEntity<DadosDetalhamentoSaque> responseSaque = restTemplate.exchange(
                    "http://localhost:8080/caixa/sacar",
                    HttpMethod.PUT,
                    requestSaque,
                    DadosDetalhamentoSaque.class
            );
        } catch (Exception e){
            assertEquals("400 : \"Não há notas disponíveis para realizar este saque\"",e.getMessage());
        }
    }
}