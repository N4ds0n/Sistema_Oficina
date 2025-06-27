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
* @author santo
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
    * @param id
    * @param nome
    * @param endereco
    * @param telefone
    * @param email
    * @param cpfAnonimizado 
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

    /**
     * Getters e Setters
     * @return 
     */
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
    
    /**
     * Retorna a lista de veiculos associada a este cliente.
     * @return um ArrayList de objetos Veiculo.
     */
    public ArrayList<Veiculo> getVeiculos() {
        return veiculos;
    }
    /**
     * Define a lista de veiculos para este cliente
     * @param veiculos  a nova lista de veiculos.
     */
    public void setVeiculos(ArrayList<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    /**
     * Métodos para manipular veiculos do cliente.
     * Adiciona um novo veiculo a lista de veiculos do cliente.
     * @param veiculo O objeto Veiculo a ser adicionado
     */
    public void adicionarVeiculo(Veiculo veiculo) {
        this.veiculos.add(veiculo);
    }

    /**
     * Substitui um veiculo existente na lista pelo seu ID.
     * @param index O ID do veiculo a ser substituido.
     * @param veiculo O novo objeto Veiculo que irá substitui-lo.
     */
    public void editarVeiculo(int index, Veiculo veiculo) {
        if (index >= 0 && index < veiculos.size()) {
            veiculos.set(index, veiculo);
        }
    }

    /**
     * Remove um veiculo da lista pelo seu ID.
     * @param index  O ID do veiculo a ser removido
     */
    public void removerVeiculo(int index) {
        if (index >= 0 && index < veiculos.size()) {
            veiculos.remove(index);
        }
    }

    /**
     * Exibe no concole a lista de veiculos deste cliente, com seus respectivos IDs.
     */
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
        /**
         * Compara dois clientes com base em seu ID.
         * @param c1 o primeiro cliente a ser comparado.
         * @param c2 o segundo cliente a ser comparado.
         * @return um valor negativo, zero ou positivo se o ID de c1 for menor, igual ou maior que o de c2.
         */
        @Override
        public int compare(Clientes c1, Clientes c2) {
            // Integer.compare e a forma mais segura e eficiente de comparar dois numeros inteiros.
            return Integer.compare(c1.getId(), c2.getId());
        }
    }
    
    /**
     * Metodo estatico para cadastrar um novo cliente.
     * @param idCliente O ID unico a ser atribuido ao novo cliente.
     * @return O objeto preenchido com os dados inseridos pelo usuario do sistema.
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
     * Metodo estaticoo para conduzir o fluxo de edição dos dados de um cliente.
     * @param cliente O objeto Cliente a ser editado
     * @return O objeto Cliente ja atualizado.
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
     * Método para editar ou adicionar veiculos a um cliente existente.
     * @param cliente O cliente cujos veiculos serão editados ou adicionados.
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
     * Método auxiliar para cadastrar os dados de um veiculo.
     * @return Um novo Objeto preenchido.
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
     * @return Uma String formatada com os principais dados do cliente.
     */
    @Override
    public String toString() {
        return String.format(
            "Cliente [ID: %d, Nome: %s, Tel: %s, Email: %s, Veiculos: %d]",
            id, nome, telefone, email, veiculos.size());
    }

    /**
     * Compara dois clientes pelo seu  ID unico.
     * @param o O objeto a ser comparado.
     * @return true se os IDs forem iguals, false caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clientes clientes = (Clientes) o;
        return id == clientes.id;
    }

    /**
     * Gera um codigo hash para o Cliente, baseado no seu ID.
     * @return  O codigo hash do objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Metodo estático para gerar o proximo ID de cliente disponível.
     * @param listaClientesExistente A lista de todos os clientes atualmente no sistema.
     * @return O proximo ID inteiro sequencial.
     */
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
