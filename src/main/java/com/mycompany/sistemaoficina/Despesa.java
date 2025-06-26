package com.mycompany.sistemaoficina;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Classe que representa as despesa da oficina, como aluguel, contas ou salarios.
 * @author santo
 */
public class Despesa{
    
    private int idDespesa;
    private String descricao;
    private double valor;
    private LocalDateTime data;
    private String categoria; // Ex: "Contas, Salarios, Fornecedores, outros...".
    
    /**
     * Construtor Padrão
     */
    public Despesa(){
        }
    
    /**
     * Construtor completo para criar uma nova despesa.
     * @param idDespesa O ID unico da despesa.
     * @param descricao Uma descricao do que e a despesa.
     * @param valor O custo da despesa.
     * @param data A data em que a despesa foi lancada.
     * @param categoria Uma categoria para organizar as despesas.
     */
    public Despesa(int idDespesa, String descricao, double valor, LocalDateTime data, String categoria) {
        this.idDespesa = idDespesa;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
    }
    
    // Getters e Setters:
    public int getIdDespesa(){
        return idDespesa;
    }
    
    public void setIdDespesa(int idDespesa){
        this.idDespesa = idDespesa;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
    
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    /**
     * Métodos Sobrescritos
     */
    @Override
    public String toString() {
        return "Despesa{" +
                "id=" + idDespesa +
                ", data=" + (data != null ? data.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A") +
                ", categoria='" + categoria + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor=" + String.format("R$ %.2f", valor) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Despesa despesa = (Despesa) o;
        return idDespesa == despesa.idDespesa;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDespesa);
    }
}