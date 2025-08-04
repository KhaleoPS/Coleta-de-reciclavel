package br.com.reciclagem.agendamento.repository;

import br.com.reciclagem.agendamento.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>, JpaSpecificationExecutor<Agendamento> {
    // A interface JpaSpecificationExecutor adiciona a funcionalidade de busca com filtros.
}