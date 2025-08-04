package br.com.reciclagem.agendamento.config;

import br.com.reciclagem.agendamento.model.TipoMaterial;
import br.com.reciclagem.agendamento.repository.TipoMaterialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private TipoMaterialRepository tipoMaterialRepository;

    @Override
    public void run(String... args) throws Exception {
        if (tipoMaterialRepository.count() == 0) {
            logger.info("Populando o banco de dados com tipos de materiais iniciais...");
            List<String> nomesMateriais = Arrays.asList("Papel", "Plástico", "Vidro", "Metal", "Eletrônicos");
            nomesMateriais.forEach(nome -> {
                TipoMaterial material = new TipoMaterial();
                material.setNome(nome);
                tipoMaterialRepository.save(material);
            });
            logger.info(">>> {} tipos de materiais cadastrados.", nomesMateriais.size());
        }
    }
}