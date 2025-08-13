package br.com.reciclagem.agendamento.model;

public enum StatusAgendamento {
    PENDENTE("Pendente"),
    AGENDADO("Agendado"),
    CONCLUIDO("Concluído"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusAgendamento(String descricao) { this.descricao = descricao; }
    public String getDescricao() { return descricao; }

    public static StatusAgendamento fromDescricao(String text) {
        for (StatusAgendamento status : StatusAgendamento.values()) {
            if (status.descricao.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Nenhum status encontrado com a descrição: " + text);
    }
}
