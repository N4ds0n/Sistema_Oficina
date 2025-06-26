package com.mycompany.sistemaoficina;

import java.util.Objects;

/**
 * Representa um fornecedor de peças para a Oficina
 * @author santo
 */
public class Fornecedor {

    private int idFornecedor;
    private String nomeFantasia;
    private String razaoSocial;
    private String cnpj;
    private String telefone;
    private String email;

    /**
     * Construtor padrao.
     */
    public Fornecedor() {
    }

    /**
     * Construtor completo para criar um novo fornecedor.
     * @param idFornecedor O ID unico do fornecedor.
     * @param nomeFantasia O nome comercial do fornecedor.
     * @param razaoSocial A razao social (nome legal) do fornecedor.
     * @param cnpj O CNPJ do fornecedor.
     * @param telefone O telefone de contato.
     * @param email O email de contato.
     */
    public Fornecedor(int idFornecedor, String nomeFantasia, String razaoSocial, String cnpj, String telefone, String email) {
        this.idFornecedor = idFornecedor;
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.email = email;
    }

    // --- Getters e Setters ---

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // --- Metodos Sobrescritos ---

    @Override
    public String toString() {
        return "Fornecedor{" +
                "id=" + idFornecedor +
                ", nomeFantasia='" + nomeFantasia + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fornecedor that = (Fornecedor) o;
        return idFornecedor == that.idFornecedor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFornecedor);
    }
}

