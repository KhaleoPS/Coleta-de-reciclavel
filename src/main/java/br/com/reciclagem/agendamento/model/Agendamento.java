package br.com.reciclagem.agendamento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "agendamento")
public class Agendamento {

    private Long id;

    private String nomeCidadao;

    private String email;

    private String telefoneCidadao;

    private String rua;

    private String numero;

    private String bairro;

    private String cidade;

    private String cep;

    private String tipoImovel; // Casa ou Apartamento

    // Campos condicionais para apartamento
    private String numeroApartamento;

    private String bloco;

    private List<TipoMaterial> tiposMaterial;

    private LocalDateTime dataHoraSugerida;

    private String status;
    private LocalDateTime dataUltimaAtualizacao;
    private String observacao;

    // Métodos utilitários para facilitar a exibição nas telas
    @Transient
    public String getEnderecoCompleto() {
        String enderecoBase = String.format("%s, %s - %s, %s - CEP: %s", rua, numero, bairro, cidade, cep);
        if ("Apartamento".equals(tipoImovel) && numeroApartamento != null && !numeroApartamento.isBlank()) {
            return String.format("%s (Apto: %s, Bloco: %s)", enderecoBase, numeroApartamento, (bloco != null && !bloco.isBlank() ? bloco : "N/A"));
        }
        return enderecoBase;
    }

    @Transient
    public String getMateriaisFormatados() {
        if (tiposMaterial == null || tiposMaterial.isEmpty()) {
            return "Nenhum material selecionado";
        }
        return String.join(", ", tiposMaterial.stream().map(TipoMaterial::getNome).toList());
    }

    // Getters e Setters

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @NotBlank(message = "O nome é obrigatório.")
    public String getNomeCidadao() { return nomeCidadao; }
    public void setNomeCidadao(String nomeCidadao) { this.nomeCidadao = nomeCidadao; }

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Por favor, insira um e-mail válido.")
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @NotBlank(message = "O telefone é obrigatório.")
    public String getTelefoneCidadao() { return telefoneCidadao; }
    public void setTelefoneCidadao(String telefoneCidadao) { this.telefoneCidadao = telefoneCidadao; }

    @NotBlank(message = "A rua é obrigatória.")
    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    @NotBlank(message = "O número é obrigatório.")
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    @NotBlank(message = "O bairro é obrigatório.")
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    @NotBlank(message = "A cidade é obrigatória.")
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    @NotBlank(message = "O CEP é obrigatório.")
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    @NotBlank(message = "Selecione o tipo de imóvel.")
    public String getTipoImovel() { return tipoImovel; }
    public void setTipoImovel(String tipoImovel) { this.tipoImovel = tipoImovel; }

    public String getNumeroApartamento() { return numeroApartamento; }
    public void setNumeroApartamento(String numeroApartamento) { this.numeroApartamento = numeroApartamento; }

    public String getBloco() { return bloco; }
    public void setBloco(String bloco) { this.bloco = bloco; }

    @NotEmpty(message = "Selecione ao menos um tipo de material.")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "agendamento_materiais",
            joinColumns = @JoinColumn(name = "agendamento_id"),
            inverseJoinColumns = @JoinColumn(name = "tipomaterial_id")
    )
    public List<TipoMaterial> getTiposMaterial() { return tiposMaterial; }
    public void setTiposMaterial(List<TipoMaterial> tiposMaterial) { this.tiposMaterial = tiposMaterial; }

    @NotNull(message = "A data e hora são obrigatórias.")
    public LocalDateTime getDataHoraSugerida() { return dataHoraSugerida; }
    public void setDataHoraSugerida(LocalDateTime dataHoraSugerida) { this.dataHoraSugerida = dataHoraSugerida; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataUltimaAtualizacao() { return dataUltimaAtualizacao; }
    public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) { this.dataUltimaAtualizacao = dataUltimaAtualizacao; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
