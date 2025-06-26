package com.mycompany.sistemaoficina;

import java.util.Objects;

public class Produto {
    
    private int idProduto;
    private String nome;
    private String descricao;
    private double precoCusto;
    private double precoVenda;
    private int quantidadeEstoque;
    private int idFornecedor;
    private String nomeFornecedor;

    /**
     * Construtor padrão.
     */
    public Produto() {
    }

    /**
     * Construtor completo para criar um novo produto.
     * @param idProduto Identificador único do produto.
     * @param nome Nome do produto.
     * @param descricao Descrição detalhada do produto.
     * @param precoCusto O preço que a oficina pagou pelo produto.
     * @param precoVenda O preço pelo qual o produto será vendido ao cliente.
     * @param quantidadeEstoque A quantidade inicial em estoque.
     * @param fornecedor O objeto Fornecedor associado a este produto.
     */
    public Produto(int idProduto, String nome, String descricao, double precoCusto, double precoVenda, int quantidadeEstoque, Fornecedor fornecedor) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.descricao = descricao;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque;
        
        if (fornecedor != null){
            this.idFornecedor = fornecedor.getIdFornecedor();
            this.nomeFornecedor = fornecedor.getNomeFantasia();
        }
    }
    
    // Getters e Setters 

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(double precoCusto) {
        this.precoCusto = precoCusto;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
    
    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }
    
     /**
     * Adiciona uma quantidade de itens ao estoque.
     * @param quantidade A quantidade a ser adicionada.
     */
    public void adicionarEstoque(int quantidade) {
        if (quantidade > 0) {
            this.quantidadeEstoque += quantidade;
        }
    }
    
     /**
     * Remove uma quantidade de itens do estoque.
     * @param quantidade A quantidade a ser removida.
     * @return true se a remoção foi bem-sucedida, false se não havia estoque suficiente.
     */
    public boolean removerEstoque(int quantidade) {
        if (quantidade > 0 && this.quantidadeEstoque >= quantidade) {
            this.quantidadeEstoque -= quantidade;
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Produto{" +
                "id=" + idProduto +
                ", nome='" + nome + '\'' +
                ", preco=" + String.format("R$%.2f", precoVenda) +
                ", qtd=" + quantidadeEstoque +
                ", fornecedor='" + nomeFornecedor + '\'' + 
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return idProduto == produto.idProduto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduto);
    }
}
