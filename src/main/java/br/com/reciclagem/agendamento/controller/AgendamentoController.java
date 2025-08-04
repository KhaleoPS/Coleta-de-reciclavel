package br.com.reciclagem.agendamento.controller;

import br.com.reciclagem.agendamento.model.Agendamento;
import br.com.reciclagem.agendamento.model.TipoMaterial;
import br.com.reciclagem.agendamento.repository.AgendamentoRepository;
import br.com.reciclagem.agendamento.repository.TipoMaterialRepository;
import br.com.reciclagem.agendamento.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Controller // @Controller é para aplicações web tradicionais (com Thymeleaf)
public class AgendamentoController {

    // @Autowired faz a injeção de dependência. O Spring nos dá uma instância do repositório.
    @Autowired
    private AgendamentoRepository agendamentoRepository;
    @Autowired
    private TipoMaterialRepository tipoMaterialRepository;
    @Autowired
    private AgendamentoService agendamentoService;

    // Este método responde à requisição GET para a raiz do site ("/")
    @GetMapping("/")
    public String exibirFormulario(Model model) {
        // Adiciona um objeto Agendamento vazio ao "model".
        // O formulário usará este objeto para preencher os dados.
        model.addAttribute("tiposImovel", Arrays.asList("Casa", "Apartamento"));
        model.addAttribute("materiaisDisponiveis", tipoMaterialRepository.findAll());
        model.addAttribute("agendamento", new Agendamento());
        // Retorna o nome do arquivo HTML que deve ser renderizado.
        return "formulario-agendamento";
    }
    
    // Este método responde à requisição POST para "/agendar"
    @PostMapping("/agendar")
    public String processarFormulario(@Valid @ModelAttribute("agendamento") Agendamento agendamento, BindingResult bindingResult, Model model) {
        // --- VALIDAÇÕES CUSTOMIZADAS ---
        // 1. Validação condicional para apartamento
        if ("Apartamento".equals(agendamento.getTipoImovel())) {
            if (agendamento.getNumeroApartamento() == null || agendamento.getNumeroApartamento().isBlank()) {
                bindingResult.rejectValue("numeroApartamento", "error.agendamento", "O número do apartamento é obrigatório.");
            }
            if (agendamento.getBloco() == null || agendamento.getBloco().isBlank()) {
                bindingResult.rejectValue("bloco", "error.agendamento", "O bloco é obrigatório. Se não houver, digite 'Não se aplica'.");
            }
        }

        // 2. Validação da data (RN001.2)
        if (agendamento.getDataHoraSugerida() != null) {
            LocalDateTime dataMinimaPermitida = calcularDataMinimaAgendamento();
            if (agendamento.getDataHoraSugerida().isBefore(dataMinimaPermitida)) {
                bindingResult.rejectValue("dataHoraSugerida", "error.agendamento", "A data da coleta deve ser pelo menos daqui há 2 dias úteis.");
            }
        }

        // --- VERIFICAÇÃO FINAL ---
        // Se houver QUALQUER erro (das anotações @Valid ou das validações customizadas), retorna ao formulário.
        if (bindingResult.hasErrors()) {
            // Repopula o modelo com as listas necessárias para renderizar o formulário novamente.
            model.addAttribute("tiposImovel", Arrays.asList("Casa", "Apartamento"));
            model.addAttribute("materiaisDisponiveis", tipoMaterialRepository.findAll());
            return "formulario-agendamento";
        }

        // RN005: O status inicial de um novo agendamento deve ser "Pendente"
        agendamento.setStatus("Pendente");
        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento); // Salva o objeto no banco de dados.

        // Redireciona para uma página de sucesso.
        // RN001.5: Passa o ID (protocolo) para a página de sucesso
        return "redirect:/sucesso/" + agendamentoSalvo.getId();
    }

    @GetMapping("/sucesso/{id}")
    public String exibirSucesso(@PathVariable("id") Long id, Model model) {
        // Busca o agendamento para exibir os detalhes na página de sucesso (RN001.6)
        agendamentoRepository.findById(id).ifPresent(agendamento -> {
            model.addAttribute("agendamento", agendamento);
        });
        return "sucesso"; // Nome do arquivo HTML
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // --- ÁREA ADMINISTRATIVA ---

    @GetMapping("/admin/agendamentos")
    public String listarAgendamentos(
            @RequestParam(required = false) LocalDate filtroData,
            @RequestParam(required = false) String filtroStatus,
            Model model) {
        model.addAttribute("agendamentos", agendamentoService.buscarComFiltros(filtroData, filtroStatus));
        model.addAttribute("statusList", Arrays.asList("Pendente", "Agendado", "Concluído", "Cancelado"));
        return "lista-agendamentos"; // Retorna o nome do novo arquivo HTML
    }

    @GetMapping("/admin/agendamentos/{id}")
    public String exibirDetalhesAgendamento(@PathVariable("id") Long id, Model model) {
        // O método findById retorna um Optional, que é uma forma segura de lidar com valores que podem ser nulos.
        agendamentoRepository.findById(id).ifPresent(agendamento -> {
            model.addAttribute("statusList", Arrays.asList("Pendente", "Agendado", "Concluído", "Cancelado"));
            model.addAttribute("agendamento", agendamento);
        });
        return "detalhe-agendamento";
    }

    @PostMapping("/admin/agendamentos/{id}/atualizar-status")
    public String atualizarStatus(
            @PathVariable("id") Long id,
            @RequestParam String status,
            @RequestParam(required = false) String observacao,
            RedirectAttributes redirectAttributes) {
        try {
            agendamentoService.atualizarStatus(id, status, observacao);
            redirectAttributes.addFlashAttribute("successMessage", "Status atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocorreu um erro ao atualizar o status.");
        }
        return "redirect:/admin/agendamentos/" + id;
    }

    /**
     * Calcula a data mínima para um agendamento, que é 2 dias úteis a partir de agora.
     * @return O LocalDateTime representando o início do dia mínimo para agendamento.
     */
    private LocalDateTime calcularDataMinimaAgendamento() {
        LocalDateTime dataResultante = LocalDateTime.now();
        int diasUteisAdicionados = 0;
        while (diasUteisAdicionados < 2) {
            dataResultante = dataResultante.plusDays(1);
            // Desconsidera Sábado (SATURDAY) e Domingo (SUNDAY)
            if (!(dataResultante.getDayOfWeek() == DayOfWeek.SATURDAY || dataResultante.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                diasUteisAdicionados++;
            }
        }
        return dataResultante.toLocalDate().atStartOfDay();
    }
}
