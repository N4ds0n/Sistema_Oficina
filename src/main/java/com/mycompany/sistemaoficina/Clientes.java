package com.mycompany.sistemaoficina;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

/**
 * Classe que representa um Cliente da oficina
 * @author santo
 */
public class Clientes implements Comparable<Clientes> {

    private int id;
    private String nome;
    private String endereco;
    private String telefone;
    private String email;
    private String cpfAnonimizado;
    private ArrayList<Veiculo> veiculos;

   /**
    * Construtor padrão.
    * Gera também uma lista de veículos vinculada ao cliente. 1..N
    */
    public Clientes() {
        this.veiculos = new ArrayList<>();
    }

    /**
     * Construtor completo para criar um novo cliente.
     * @param id
     * @param nome
     * @param endereco
     * @param telefone
     * @param email
     * @param cpfAnonimizado 
     */
    public Clientes(int id, String nome, String endereco, String telefone, String email, String cpfAnonimizado) {
        this(); // Chama o construtor padrão para inicializar a lista de veículos
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.cpfAnonimizado = cpfAnonimizado;
    }

    /**
     * Getters e Setters
     * @return 
     */
    public int getId() {
        return id; 
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id; 
    }

    /**
     *
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     *
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     *
     * @return
     */
    public String getEndereco() {
    return endereco;
}

    /**
     *
     * @param endereco
     */
    public void setEndereco(String endereco) {
    this.endereco = endereco;
}

    /**
     *
     * @return
     */
    public String getTelefone() {
    return telefone;
}

    /**
     *
     * @param telefone
     */
    public void setTelefone(String telefone) {
    this.telefone = telefone;
}

    /**
     *
     * @return
     */
    public String getEmail() {
    return email;
}

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
    this.email = email;
}

    /**
     *
     * @return
     */
    public ArrayList<Veiculo> getVeiculos() { return veiculos; }

    /**
     *
     * @param veiculos
     */
    public void setVeiculos(ArrayList<Veiculo> veiculos) { this.veiculos = veiculos; }


    // --- MÉTODOS DE MANIPULAÇÃO DE DADOS (permanecem aqui) ---

    /**
     *
     * @param veiculo
     */
    public void adicionarVeiculo(Veiculo veiculo) {
        this.veiculos.add(veiculo);
    }

    /**
     *
     * @param index
     * @param veiculo
     */
    public void editarVeiculo(int index, Veiculo veiculo) {
        if (index >= 0 && index < veiculos.size()) {
            veiculos.set(index, veiculo);
        }
    }

    /**
     *
     * @param index
     */
    public void removerVeiculo(int index) {
        if (index >= 0 && index < veiculos.size()) {
            veiculos.remove(index);
        }
    }

    // --- MÉTODOS PADRÃO DO JAVA (permanecem aqui) ---

    /**
     *
     * @param outroCliente
     * @return
     */
    @Override
    public int compareTo(Clientes outroCliente) {
        return this.nome.compareToIgnoreCase(outroCliente.getNome());
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("Cliente [ID: %d, Nome: %s, Tel: %s]", id, nome, telefone);
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clientes clientes = (Clientes) o;
        return id == clientes.id;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // O Comparator também pode ficar aqui como uma classe interna estática

    /**
     *
     */
    public static class ClientePorIdComparator implements Comparator<Clientes> {

        /**
         *
         * @param c1
         * @param c2
         * @return
         */
        @Override
        public int compare(Clientes c1, Clientes c2) {
            return Integer.compare(c1.getId(), c2.getId());
        }
    }
}