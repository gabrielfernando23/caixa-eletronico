package br.com.brq.caixaeletronico.caixa;

import br.com.brq.caixaeletronico.model.CaixaEletronico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaixaEletronicoRepository extends JpaRepository<CaixaEletronico,Long> {
}
