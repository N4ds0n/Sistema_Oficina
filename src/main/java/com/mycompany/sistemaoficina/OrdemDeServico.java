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
 * @author santo
 */
public class OrdemDeServico {

    private final int idOrdemDeServico;
    private final int idAgendamento;
    private final String nomeCliente;
    private final String modeloVeiculo;
    private final String placaVeiculo;
    private final List<Servico> servicosRealizados;
    private final List<Produto> pecasUtilizadas;
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

    /**
     * Métodos de Negócio
     * Adiciona um servico a esta Ordem de Servico, desde que ela esteja "Aberta".
     * Apos adicionar, o valor total e recalculado automaticamente.
     * @param servico O objeto Servico a ser adicionado a lista.
     */
    public void adicionarServico(Servico servico) {
        if (this.status.equals("Aberta")) {
            this.servicosRealizados.add(servico);
            calcularEAtualizarValorTotal();
        } else {
            System.out.println("Erro: Nao e possivel adicionar itens a uma Ordem de Servico finalizada.");
        }
    }

    /**
     * Adiciona uma peca a esta Ordem de Servico, desde que ela esteja "Aberta".
     * Apos adicionar, o valor total e recalculado automaticamente.
     * @param peca O objeto Produto a ser adicionado a lista.
     */
    public void adicionarPeca(Produto peca) {
        if (this.status.equals("Aberta")) {
            this.pecasUtilizadas.add(peca);
            calcularEAtualizarValorTotal();
        } else {
            System.out.println("Erro: Nao e possivel adicionar itens a uma Ordem de Servico finalizada.");
        }
    }

    /**
     * Metodo privado para calcular o valor total da OS.
     * E chamado internamente sempre que um novo item (servico ou peca) e adicionado.
     */
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

    /**
     * Finaliza a Ordem de Servico, mudando seu status para "Finalizada".
     * Isso impede que novos itens sejam adicionados e atualiza a data de emissao.
     */
    public void finalizar() {
        this.status = "Finalizada";
        this.dataEmissao = LocalDateTime.now();
    }

    // Getters 

    /**
     *
     * @return
     */

    public int getIdOrdemDeServico() {
        return idOrdemDeServico;
    }

    /**
     *
     * @return
     */
    public int getIdAgendamento() {
        return idAgendamento;
    }

    /**
     *
     * @return
     */
    public String getNomeCliente() {
        return nomeCliente;
    }

    /**
     *
     * @return
     */
    public String getModeloVeiculo() {
        return modeloVeiculo;
    }

    /**
     *
     * @return
     */
    public String getPlacaVeiculo() {
        return placaVeiculo;
    }
    
    /**
     *
     * @return
     */
    public List<Servico> getServicosRealizados() {
        return servicosRealizados;
    }

    /**
     *
     * @return
     */
    public List<Produto> getPecasUtilizadas() {
        return pecasUtilizadas;
    }

    /**
     *
     * @return
     */
    public double getValorTotal() {
        return valorTotal;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * Métodos Sobrescritos:
     * Fornece uma representacao textual resumida e formatada da Ordem de Servico.
     * Ideal para listagens rapidas.
     * @return Uma String contendo os principais dados da OS.
     */
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

    /**
     * Compara duas Ordens de Servico pelo seu ID unico.
     * @param o O objeto a ser comparado.
     * @return true se os IDs forem iguais, false caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdemDeServico that = (OrdemDeServico) o;
        return idOrdemDeServico == that.idOrdemDeServico;
    }

    /**
     * Gera um codigo hash para a Ordem de Servico, baseado no seu ID.
     * @return O codigo hash do objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idOrdemDeServico);
    }
}
