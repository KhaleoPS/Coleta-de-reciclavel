package br.com.reciclagem.agendamento.controller;

import br.com.reciclagem.agendamento.model.Agendamento;
import org.junit.jupiter.api.Test;
import br.com.reciclagem.agendamento.model.TipoMaterial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class AgendamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveFalharAoAgendarComDataMenorQueDoisDiasUteis() throws Exception {
        // ARRANGE (Preparar): Cria um agendamento com data para amanhã, o que é inválido.
        Agendamento agendamentoInvalido = new Agendamento();
        agendamentoInvalido.setNomeCidadao("Teste QA");
        agendamentoInvalido.setEmail("teste@qa.com");
        agendamentoInvalido.setTelefoneCidadao("(11) 98888-7777");
        agendamentoInvalido.setRua("Rua do Teste");
        agendamentoInvalido.setNumero("123");
        agendamentoInvalido.setBairro("Centro");
        agendamentoInvalido.setCidade("Testelândia");
        agendamentoInvalido.setCep("99999-000");
        agendamentoInvalido.setTipoImovel("Casa");
        TipoMaterial materialTeste = new TipoMaterial();
        materialTeste.setNome("Papel");
        agendamentoInvalido.setTiposMaterial(Collections.singletonList(materialTeste));
        // Define uma data inválida (amanhã)
        agendamentoInvalido.setDataHoraSugerida(LocalDateTime.now().plusDays(1));

        // ACT (Agir) & ASSERT (Verificar)
        mockMvc.perform(post("/agendar")
                        .flashAttr("agendamento", agendamentoInvalido)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("formulario-agendamento"));
    }

    @Test
    void deveFalharAoAgendarSemNomeCidadao() throws Exception {
        // ARRANGE: Cria um agendamento válido, mas deixa o nome do cidadão em branco.
        Agendamento agendamentoInvalido = new Agendamento();
        agendamentoInvalido.setNomeCidadao(""); // Nome está em branco (inválido)
        agendamentoInvalido.setEmail("teste@qa.com");
        agendamentoInvalido.setTelefoneCidadao("(11) 98888-7777");
        agendamentoInvalido.setRua("Rua do Teste");
        agendamentoInvalido.setNumero("123");
        agendamentoInvalido.setBairro("Centro");
        agendamentoInvalido.setCidade("Testelândia");
        agendamentoInvalido.setCep("99999-000");
        agendamentoInvalido.setTipoImovel("Casa");
        TipoMaterial materialTeste = new TipoMaterial();
        materialTeste.setNome("Papel");
        agendamentoInvalido.setTiposMaterial(Collections.singletonList(materialTeste));
        agendamentoInvalido.setDataHoraSugerida(LocalDateTime.now().plusDays(3)); // Data válida

        // ACT & ASSERT: Espera que o formulário seja retornado com um erro no campo 'nomeCidadao'.
        mockMvc.perform(post("/agendar")
                        .flashAttr("agendamento", agendamentoInvalido)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("formulario-agendamento"))
                .andExpect(model().attributeHasFieldErrors("agendamento", "nomeCidadao"));
    }

    @Test
    void deveFalharAoAgendarSemEndereco() throws Exception {
        // ARRANGE: Cria um agendamento válido, mas deixa o campo de endereço (rua) em branco.
        Agendamento agendamentoInvalido = new Agendamento();
        agendamentoInvalido.setNomeCidadao("Teste QA");
        agendamentoInvalido.setEmail("teste@qa.com");
        agendamentoInvalido.setTelefoneCidadao("(11) 98888-7777");
        agendamentoInvalido.setRua(""); // Rua está em branco (inválido)
        agendamentoInvalido.setNumero("123");
        agendamentoInvalido.setBairro("Centro");
        agendamentoInvalido.setCidade("Testelândia");
        agendamentoInvalido.setCep("99999-000");
        agendamentoInvalido.setTipoImovel("Casa");
        TipoMaterial materialTeste = new TipoMaterial();
        materialTeste.setNome("Papel");
        agendamentoInvalido.setTiposMaterial(Collections.singletonList(materialTeste));
        agendamentoInvalido.setDataHoraSugerida(LocalDateTime.now().plusDays(3)); // Data válida

        // ACT & ASSERT: Espera que o formulário seja retornado com um erro no campo 'rua'.
        mockMvc.perform(post("/agendar")
                        .flashAttr("agendamento", agendamentoInvalido)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("formulario-agendamento"))
                .andExpect(model().attributeHasFieldErrors("agendamento", "rua"));
    }
}
