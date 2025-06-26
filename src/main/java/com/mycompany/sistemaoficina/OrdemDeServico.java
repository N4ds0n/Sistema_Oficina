package com.mycompany.sistemaoficina;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa a Ordem de Servico (OS), a fatura de um agendamento.
 * Contem todos os servicos realizados e pecas utilizadas.
 * ARMAZENA o ID do agendamento e alguns dados do cliente/veiculo para consulta rapida.
 */
public class OrdemDeServico {

    private int idOrdemDeServico;
    private int idAgendamento;
    private String nomeCliente;
    private String modeloVeiculo;
    private String placaVeiculo;
    private List<Servico> servicosRealizados;
    private List<Produto> pecasUtilizadas;
    private double valorTotal;
    private LocalDateTime dataEmissao;
    private String status;

    /**
     * Construtor. Cria uma Ordem de Servico vinculada a um agendamento.
     * @param idOrdemDeServico O ID unico desta OS.
     * @param agendamento O agendamento que esta dando origem a OS.
     */
    public OrdemDeServico(int idOrdemDeServico, Agendamento agendamento) {
        this.idOrdemDeServico = idOrdemDeServico;
        this.idAgendamento = agendamento.getIdAgendamento();
        this.nomeCliente = agendamento.getCliente().getNome();
        this.modeloVeiculo = agendamento.getVeiculo().getModelo();
        this.placaVeiculo = agendamento.getVeiculo().getPlaca();
        this.servicosRealizados = new ArrayList<>();
        this.pecasUtilizadas = new ArrayList<>();
        this.dataEmissao = LocalDateTime.now();
        this.valorTotal = 0.0;
        this.status = "Aberta";
    }

    // --- Metodos de Negocio ---

    public void adicionarServico(Servico servico) {
        if (this.status.equals("Aberta")) {
            this.servicosRealizados.add(servico);
            calcularEAtualizarValorTotal();
        } else {
            System.out.println("Erro: Nao e possivel adicionar itens a uma Ordem de Servico finalizada.");
        }
    }

    public void adicionarPeca(Produto peca) {
        if (this.status.equals("Aberta")) {
            this.pecasUtilizadas.add(peca);
            calcularEAtualizarValorTotal();
        } else {
            System.out.println("Erro: Nao e possivel adicionar itens a uma Ordem de Servico finalizada.");
        }
    }

    private void calcularEAtualizarValorTotal() {
        double total = 0.0;
        for (Servico servico : servicosRealizados) {
            total += servico.getValor();
        }
        for (Produto peca : pecasUtilizadas) {
            total += peca.getPrecoVenda();
        }
        this.valorTotal = total;
    }

    public void finalizar() {
        this.status = "Finalizada";
        this.dataEmissao = LocalDateTime.now();
    }

    // Getters 

    public int getIdOrdemDeServico() {
        return idOrdemDeServico;
    }

    public int getIdAgendamento() {
        return idAgendamento;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getModeloVeiculo() {
        return modeloVeiculo;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }
    
    public List<Servico> getServicosRealizados() {
        return servicosRealizados;
    }

    public List<Produto> getPecasUtilizadas() {
        return pecasUtilizadas;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public String getStatus() {
        return status;
    }

    // Metodos Sobrescritos 

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OS_ID: ").append(idOrdemDeServico);
        sb.append(" | Cliente: ").append(nomeCliente);
        sb.append(" | Veiculo: ").append(modeloVeiculo).append(" (").append(placaVeiculo).append(")");
        sb.append(" | Status: ").append(status);
        sb.append(String.format(" | Valor: R$%.2f", valorTotal));
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdemDeServico that = (OrdemDeServico) o;
        return idOrdemDeServico == that.idOrdemDeServico;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrdemDeServico);
    }
}
