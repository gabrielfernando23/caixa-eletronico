package br.com.brq.caixaeletronico.infra.exception;

import br.com.brq.caixaeletronico.ValidacaoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity tratarErroValidacao(ValidacaoException vx) {return ResponseEntity.badRequest().body(vx.getMessage());
    }
}
