
package com.mycompany.sistemaoficina;

import java.util.Objects;

/**
 * Classe que representa um serviço a ser oferecido pela oficina, como "Troca de Oleo" ou Alinhamento".
 * Funciona como um item no catalofo de serviços da empresa.
 * @author santo
 */
public class Servico {
    
    private int idServico;
    private String descricao;
    private double valor;
    
    /**
     * Construtor padrão
     */
    public Servico(){
    }
    
    /**
     * Construtor completo para criar um novo servico.
     * @param idServico Identificador unico do servico.
     * @param descricao Descricao do servico (ex: "Troca de Oleo do Motor").
     * @param valor O preco padrao para este servico.
     */
    public Servico(int idServico, String descricao, double valor) {
        this.idServico = idServico;
        this.descricao = descricao;
        this.valor = valor;
    }
    
    /**
     * Getters e Setters
     * @return 
     */
    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
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

    /**
     * Métodos Sobrescritos.
     * Fornece uma representação Textual formatada do Serviço.
     * @return Uma String contendo os principais dados do serviço.
     */
    @Override
    public String toString() {
        return "Servico{" +
                "idServico=" + idServico +
                ", descricao='" + descricao + '\'' +
                ", valor=" + String.format("R$%.2f", valor) +
                '}';
    }

    /**
     * Compara dois Serviços pelo seu ID unico.
     * @param o O objeto a ser comparado.
     * @return  true se os IDs forem iguais, false caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servico servico = (Servico) o;
        return idServico == servico.idServico;
    }

    /**
     * Gera um codigo hash para o Servico, baseado no seu ID
     * @return O codigo hash do objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idServico);
    }
}
