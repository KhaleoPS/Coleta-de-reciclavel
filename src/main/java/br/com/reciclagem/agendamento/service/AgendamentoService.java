package br.com.reciclagem.agendamento.service;

import br.com.reciclagem.agendamento.model.Agendamento;
import br.com.reciclagem.agendamento.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    public List<Agendamento> buscarComFiltros(LocalDate filtroData, String filtroStatus) {
        // RN003.4: Ordena por data da coleta (mais próximos primeiro) por padrão.
        Sort sort = Sort.by(Sort.Direction.ASC, "dataHoraSugerida");

        // RN003.3: Cria especificações para filtros dinâmicos.
        Specification<Agendamento> spec = Specification.where(null);

        if (filtroData != null) {
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("dataHoraSugerida"), filtroData.atStartOfDay(), filtroData.atTime(23, 59, 59)));
        }

        if (StringUtils.hasText(filtroStatus)) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), filtroStatus));
        }

        return agendamentoRepository.findAll(spec, sort);
    }

    public Optional<Agendamento> atualizarStatus(Long id, String novoStatus, String observacao) {
        Optional<Agendamento> agendamentoOpt = agendamentoRepository.findById(id);
        if (agendamentoOpt.isPresent()) {
            Agendamento agendamento = agendamentoOpt.get();
            agendamento.setStatus(novoStatus);
            agendamento.setDataUltimaAtualizacao(LocalDateTime.now()); // RN005.2

            // RN005.3: Validação da observação
            if ("Concluído".equals(novoStatus) || "Cancelado".equals(novoStatus)) {
                if (!StringUtils.hasText(observacao)) {
                    throw new IllegalArgumentException("A observação é obrigatória para status Concluído ou Cancelado.");
                }
                agendamento.setObservacao(observacao);
            }
            return Optional.of(agendamentoRepository.save(agendamento));
        }
        return Optional.empty();
    }
}
