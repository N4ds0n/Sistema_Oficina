package com.mycompany.sistemaoficina;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Objects;

/**
* Classe que representa um cliente do sistema de oficina.
* Armazena informacoes pessoais e uma lista de veiculos associados ao cliente.
* Implementa Comparable para permitir a ordenacao padrao por nome.
*/
public class Clientes implements Comparable<Clientes> { // CORRECAO: "Comparable" com 'C' maiusculo

    private int id;
    private String nome;
    private String endereco;
    private String telefone;
    private String email;
    private String cpfAnonimizado;
    private ArrayList<Veiculo> veiculos;

    /**
     * Construtor padrao que inicializa a lista de veiculos.
     */
    public Clientes() {
        this.veiculos = new ArrayList<>();
    }

    /**
     * Construtor com todos os dados do cliente, incluindo o ID.
     */
    public Clientes(int id, String nome, String endereco, String telefone, String email, String cpfAnonimizado) {
        this(); // Chama o construtor padrao para inicializar 'veiculos'
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.cpfAnonimizado = cpfAnonimizado;
    }

    // --- Getters e Setters ---
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
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
    public String getCpfAnonimizado() {
        return cpfAnonimizado;
    }
    public void setCpfAnonimizado(String cpfAnonimizado) {
        this.cpfAnonimizado = cpfAnonimizado;
    }
    public ArrayList<Veiculo> getVeiculos() {
        return veiculos;
    }
    public void setVeiculos(ArrayList<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    // --- Metodos para manipular veiculos do cliente ---
    public void adicionarVeiculo(Veiculo veiculo) {
        this.veiculos.add(veiculo);
    }

    public void editarVeiculo(int index, Veiculo veiculo) {
        if (index >= 0 && index < veiculos.size()) {
            veiculos.set(index, veiculo);
        }
    }

    public void removerVeiculo(int index) {
        if (index >= 0 && index < veiculos.size()) {
            veiculos.remove(index);
        }
    }

    public void listarVeiculos() {
        if (veiculos.isEmpty()) {
            System.out.println("Nenhum veiculo cadastrado para este cliente.");
            return;
        }
        System.out.println("Veiculos do cliente " + this.getNome() + ":");
        for (int i = 0; i < veiculos.size(); i++) {
            System.out.println("  " + i + ". " + veiculos.get(i));
        }
    }

    /**
     * Implementação do método da interface Comparable
     * Define a "ordem natural" dos clientes como sendo alfabetica pelo nome.
     * Este metodo sera usado por Collections.sort() quando nenhuma outra ordem for especificada.
     * @param outroCliente O outro objeto Cliente para comparar.
     * @return um valor negativo se este nome vier antes, positivo se vier depois, e 0 se forem iguais.
     */
    @Override
    public int compareTo(Clientes outroCliente) {
        // compareToIgnoreCase faz a comparacao alfabetica sem diferenciar maiusculas de minusculas.
        return this.nome.compareToIgnoreCase(outroCliente.getNome());
    }

    /**
     * Um Comparator para fornecer uma forma alternativa de ordenar clientes, pelo seu ID.
     * Uma classe pode ter varios Comparators para diferentes tipos de ordenacao.
     */
    public static class ClientePorIdComparator implements Comparator<Clientes> {
        @Override
        public int compare(Clientes c1, Clientes c2) {
            // Integer.compare e a forma mais segura e eficiente de comparar dois numeros inteiros.
            return Integer.compare(c1.getId(), c2.getId());
        }
    }
    
    /**
     * Metodo para cadastrar um novo cliente com entrada via terminal.
     */
    public static Clientes cadastrarCliente(int idCliente) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Endereco: ");
        String endereco = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("CPF Anonimizado: ");
        String cpfAnonimizado = scanner.nextLine();
        
        Clientes cliente = new Clientes(idCliente, nome, endereco, telefone, email, cpfAnonimizado);

        System.out.print("Modelo do veiculo: ");
        String modelo = scanner.nextLine();
        System.out.print("Placa do veiculo: ");
        String placa = scanner.nextLine();
        System.out.print("Cor do veiculo: ");
        String cor = scanner.nextLine();
        System.out.print("Ano do veiculo: ");
        int ano = Integer.parseInt(scanner.nextLine());

        Veiculo veiculo = new Veiculo(modelo, placa, cor, ano);
        cliente.adicionarVeiculo(veiculo);
        
        return cliente;
    }

    /**
     * Metodo estatico para editar os dados de um cliente.
     */
    public static Clientes editarCliente(Clientes cliente){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\nEditando cliente: " + cliente.getNome());
        System.out.println(" ");
        
        System.out.print("Novo nome (atual: " + cliente.getNome() + ", deixe em branco para manter): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isEmpty()) cliente.setNome(novoNome);
        
        System.out.print("Novo endereco (" + cliente.getEndereco() + ", deixe em branco para manter): ");
        String novoEndereco = scanner.nextLine();
        if (!novoEndereco.isEmpty()) cliente.setEndereco(novoEndereco);
        
        System.out.print("Novo telefone (" + cliente.getTelefone() + ", deixe em branco para manter): ");
        String novoTelefone = scanner.nextLine();
        if (!novoTelefone.isEmpty()) cliente.setTelefone(novoTelefone);
        
        System.out.print("Novo email (" + cliente.getEmail() + ", deixe em branco para manter): ");
        String novoEmail = scanner.nextLine();
        if (!novoEmail.isEmpty()) cliente.setEmail(novoEmail);
        
        System.out.print("Novo CPF Anonimizado (" + cliente.getCpfAnonimizado() + ", deixe em branco para manter): ");
        String novoCpf = scanner.nextLine();
        if (!novoCpf.isEmpty()) cliente.setCpfAnonimizado(novoCpf);
        
        System.out.print("Deseja editar os veiculos desse cliente? (s/n): ");
        String resposta = scanner.nextLine();
        if (resposta.equalsIgnoreCase("s")) {
            editarVeiculos(cliente);
        }

        System.out.println("Cliente atualizado com sucesso!");
        return cliente;
    }

    /**
     * Metodo para editar ou adicionar veiculos ao cliente.
     */
    public static void editarVeiculos(Clientes cliente) {
        Scanner scanner = new Scanner(System.in);
        cliente.listarVeiculos();
        
        System.out.print("\nEscolha o INDICE do veiculo para editar (ou digite -1 para adicionar um novo): "); // Texto corrigido
        int index = Integer.parseInt(scanner.nextLine());

        if (index == -1) {
            Veiculo veiculo = cadastrarVeiculo();
            cliente.adicionarVeiculo(veiculo);
            System.out.println("Veiculo adicionado com sucesso!");
        } else {
            if (index >= 0 && index < cliente.getVeiculos().size()) {
                Veiculo veiculoAtualizado = cadastrarVeiculo();
                cliente.editarVeiculo(index, veiculoAtualizado);
                System.out.println("Veiculo editado com sucesso!");
            } else {
                System.out.println("Indice invalido!");
            }
        }
    }

    /**
     * Metodo para cadastrar um veiculo.
     */
    public static Veiculo cadastrarVeiculo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Modelo do veiculo: ");
        String modelo = scanner.nextLine();
        System.out.print("Placa do veiculo: ");
        String placa = scanner.nextLine();
        System.out.print("Cor do veiculo: ");
        String cor = scanner.nextLine();
        System.out.print("Ano do veiculo: ");
        int ano = Integer.parseInt(scanner.nextLine());
        
        return new Veiculo(modelo, placa, cor, ano);
    }
    
    /**
     * Representacao textual resumida do cliente.
     */
    
    /**
     * Representacao textual resumida do cliente.
     */
    @Override
    public String toString() {
        return String.format(
            "Cliente [ID: %d, Nome: %s, Tel: %s, Email: %s, Veiculos: %d]",
            id, nome, telefone, email, veiculos.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clientes clientes = (Clientes) o;
        return id == clientes.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static int gerarProximoIdCliente(List<Clientes> listaClientesExistente) {
        int maxId = 0;
        if (listaClientesExistente != null) {
            for (Clientes c : listaClientesExistente) {
                if (c.getId() > maxId) {
                    maxId = c.getId();
                }
            }
        }
        return maxId + 1;
    }
}
