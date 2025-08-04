package br.com.reciclagem.agendamento.repository;

import br.com.reciclagem.agendamento.model.TipoMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoMaterialRepository extends JpaRepository<TipoMaterial, Long> {
    // Métodos de busca customizados podem ser adicionados aqui
}