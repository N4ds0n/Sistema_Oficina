package com.mycompany.sistemaoficina;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;

/**
 * Classe que representa um agendamento de servico na oficina.
 * Armazena detalhes sobre o cliente, veiculo, data/hora e status do agendamento.
 * Implementa Comparable para permitir a ordenação padrão por data
 * @author santo
 */
public class Agendamento implements Comparable<Agendamento> {

    private int idAgendamento;
    private Clientes cliente;
    private Veiculo veiculo;
    private LocalDateTime dataHora;
    private String descricaoProblema;
    private String status;
    private Funcionario mecanicoResponsavel;
    private double valorRetidoCancelamento;
    private Elevador elevadorAlocado; 

    /**
     * Construtor padrao da classe Agendamento.
     * Define o status inicial como "Agendado".
     * E utilizado pela biblioteca Gson para criar objetos a partir do JSON.
     */
    public Agendamento() {
        this.status = "Agendado";
        this.elevadorAlocado = null; 
    }

    /**
     * Construtor completo para criar um novo agendamento com os dados iniciais.
     * @param idAgendamento O ID unico do agendamento.
     * @param cliente O objeto Cliente associado.
     * @param veiculo O objeto Veiculo associado.
     * @param dataHora A data e hora do agendamento.
     * @param descricaoProblema A descricao inicial do problema do veiculo.
     */
    public Agendamento(int idAgendamento, Clientes cliente, Veiculo veiculo, LocalDateTime dataHora, String descricaoProblema) {
        this();
        this.idAgendamento = idAgendamento;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataHora = dataHora;
        this.descricaoProblema = descricaoProblema;
        this.valorRetidoCancelamento = 0.0;
        this.mecanicoResponsavel = null; // Inicializa como null
        // O atributo elevadorAlocado já é inicializado como null pela chamada a this()
    }

    /**
     * Getters e Setters 
     */
    public Elevador getElevadorAlocado() {
        return elevadorAlocado;
    }

    public void setElevadorAlocado(Elevador elevadorAlocado) {
        this.elevadorAlocado = elevadorAlocado;
    }

    public int getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(int idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getDescricaoProblema() {
        return descricaoProblema;
    }

    public void setDescricaoProblema(String descricaoProblema) {
        this.descricaoProblema = descricaoProblema;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Funcionario getMecanicoResponsavel() {
        return mecanicoResponsavel;
    }

    public void setMecanicoResponsavel(Funcionario mecanicoResponsavel) {
        this.mecanicoResponsavel = mecanicoResponsavel;
    }

    public double getValorRetidoCancelamento() {
        return valorRetidoCancelamento;
    }

    public void setValorRetidoCancelamento(double valorRetidoCancelamento) {
        this.valorRetidoCancelamento = valorRetidoCancelamento;
    }
    
    /**
     * Define a "ordem natural" dos agendamentos como sendo pela data e hora.
     * @param outroAgendamento O outro objeto Agendamento para comparar.
     * @return um valor negativo se esta data vier antes, positivo se vier depois, e 0 se forem iguais.
     */
    @Override
    public int compareTo(Agendamento outroAgendamento) {
        // LocalDateTime ja implementa a logica de comparacao de datas.
        if (this.dataHora == null && outroAgendamento.dataHora == null) {
            return 0;
        }
        if (this.dataHora == null) {
            return 1; //Coloca os agendamentos sem data no final da lista.
        }
        if (outroAgendamento.dataHora == null) {
            return -1; // Coloca os agendamentos sem data no final da lista.
        }
        return this.dataHora.compareTo(outroAgendamento.dataHora);
    }

   /**
    * Um comparator para fornecer uma forma alternativa de ordenar agendamentos, pelo seu status.
    * Permite agrupar os agendamentos por "Agendado", "Em Manutenção", etc...
    */
    public static class AgendamentoPorStatusComparator implements Comparator<Agendamento> {
        /**
         * Compara dois agendamentos por ordem alfabética.
         * @param a1 o primeiro agendamento a ser comparado.
         * @param a2 o segundo agendamento a ser comparado.
         * @return um valor negativo, zero ou positivo se o status de a1 for menor, igual ou maior que o de a2.
         */
        public int compare(Agendamento a1, Agendamento a2) {
            return a1.getStatus().compareToIgnoreCase(a2.getStatus());
        }
    }


    /**
     * Sobrescreve o metodo toString() para fornecer uma representacao legivel do Agendamento.
     * @return Uma String formatada com os detalhes do agendamento.
     */
     @Override
    public String toString() {
        String infoElevador = "N/A";
        if (elevadorAlocado != null) {
            infoElevador = "Nº " + elevadorAlocado.getNumero();
        }
    
        return "Agendamento{" +
                "ID=" + idAgendamento +
                ", Cliente='" + (cliente != null ? cliente.getNome() : "N/A") + '\'' +
                ", Veiculo='" + (veiculo != null ? veiculo.getModelo() + " (" + veiculo.getPlaca() + ")" : "N/A") + '\'' +
                ", Data/Hora=" + (dataHora != null ? dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A") +
                ", Status='" + status + '\'' +
                ", Elevador='" + infoElevador + '\'' +
                ", Problema='" + descricaoProblema + '\'' +
                ", Mecanico='" + (mecanicoResponsavel != null ? mecanicoResponsavel.getNome() : "Nao Atribuido") + '\'' +
                ", Valor Retido=" + String.format("%.2f", valorRetidoCancelamento) +
                '}';
    }

    /**
     * Sobrescreve o metodo equals() para comparar objetos Agendamento.
     * Dois agendamentos sao considerados iguais se tiverem o mesmo ID.
     * @param o O objeto a ser comparado.
     * @return true se os objetos forem iguais, false caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agendamento that = (Agendamento) o;
        return idAgendamento == that.idAgendamento;
    }

    /**
     * Sobrescreve o metodo hashCode() para gerar um codigo hash para o objeto Agendamento.
     * Baseia-se no ID do agendamento.
     * @return O codigo hash do objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idAgendamento);
    }
}