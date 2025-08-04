package br.com.reciclagem.agendamento.controller;

import br.com.reciclagem.agendamento.model.TipoMaterial;
import br.com.reciclagem.agendamento.repository.TipoMaterialRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/materiais")
public class TipoMaterialController {

    @Autowired
    private TipoMaterialRepository tipoMaterialRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("materiais", tipoMaterialRepository.findAll());
        return "admin/lista-materiais";
    }

    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        model.addAttribute("material", new TipoMaterial());
        return "admin/formulario-material";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        tipoMaterialRepository.findById(id).ifPresent(material -> model.addAttribute("material", material));
        return "admin/formulario-material";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid TipoMaterial material, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/formulario-material";
        }
        tipoMaterialRepository.save(material);
        return "redirect:/materiais";
    }

    @GetMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {
        // Adicionar verificação se o material está em uso antes de remover
        tipoMaterialRepository.deleteById(id);
        return "redirect:/materiais";
    }
}