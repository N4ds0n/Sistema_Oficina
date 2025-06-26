package com.mycompany.sistemaoficina.gerenciadores; 

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.sistemaoficina.Clientes; 
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Classe responsavel por gerenciar todas as operacoes relacionadas a Clientes.
 * Inclui carregamento, salvamento e operacoes CRUD (Criar, Ler, Atualizar, Excluir).
 */
public class GerenciadorClientes {

    // Caminho do arquivo JSON para clientes
    private static final String ARQUIVO_CLIENTES_JSON = "clientes.json";

    // Lista de clientes gerenciada por esta classe
    private List<Clientes> listaClientes;

    /**
     * Construtor do GerenciadorClientes.
     * Carrega os dados dos clientes do arquivo JSON ao ser instanciado.
     */
    public GerenciadorClientes() {
        this.listaClientes = carregarDadosClientes();
    }

    /**
     * Retorna a lista de clientes.
     * @return A lista de clientes em memoria.
     */
    public List<Clientes> getListaClientes() {
        return listaClientes;
    }

    /**
     * Submenu para gerenciar os clientes (cadastro, edicao, exclusao, listagem).
     * Este metodo agora pertence ao GerenciadorClientes.
     */
    public void gerenciarClientes() { // Metodo publico para ser chamado pela SistemaOficina
        Scanner scanner = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\n=== GERENCIAR CLIENTES ===");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Editar Cliente");
            System.out.println("3. Excluir Cliente");
            System.out.println("4. Listar Clientes(Com Ordenacao)");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e){
                System.out.println("Erro: Opcao invalida. Digite um numero.");
                opcao = -1; // Forca a repeticao do loop
            }

            switch (opcao) {
                case 1:
                    cadastrarCliente(scanner); // Chama o metodo de cadastro
                    break;
                case 2:
                    editarCliente(scanner); // Chama o metodo de edicao
                    break;
                case 3:
                    excluirCliente(scanner); // Chama o metodo de exclusao
                    break;
                case 4:
                    listarClientesComOrdenacao(scanner); // Chama o metodo de listagem
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    /**
     * Método para listar clientes com opçoes de ordenação
     * Apresenta um submenu para o usuario escolher como ordenar a lista de clientes
     * antes de exibi-la.
     * @param scanner Scanner para entrada de dados do usuario.
     */
     private void listarClientesComOrdenacao(Scanner scanner) {
        if (listaClientes.isEmpty()) {
            System.out.println("\nNenhum cliente cadastrado para listar.");
            return;
        }

        System.out.println("\n--- Opcoes de Ordenacao ---");
        System.out.println("1. Ordenar por Nome (A-Z)");
        System.out.println("2. Ordenar por ID (Crescente)");
        System.out.print("Escolha como deseja ordenar a lista: ");
        
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            switch (escolha){
                case 1:
                    // Usa a ordenacao natural (por nome) definida com "implements Comparable"
                    Collections.sort(this.listaClientes);
                    System.out.println("\n=== LISTA DE CLIENTES (Ordenada por Nome) ===");
                    break;
                case 2:
                    // Usa o Comparator especifico que criamos para ordenar por ID
                    this.listaClientes.sort(new Clientes.ClientePorIdComparator());
                    System.out.println("\n=== LISTA DE CLIENTES (Ordenada por ID) ===");
                    break;
                default:
                    System.out.println("Opcao invalida. Listando na ordem atual.");
                    System.out.println("\n=== LISTA DE CLIENTES ===");
                    break;
            }
        } catch (NumberFormatException e){
            System.out.println("Opcao invalida. Listando na ordem atual.");
        }
        
        // Imprime a lita (Agora ordenada)
        for (Clientes c : this.listaClientes) {
            System.out.println("ID: " + c.getId() + " | Nome: " + c.getNome() + " | CPF: " + c.getCpfAnonimizado());
        }
    }
    
    
    /**
     * Cadastra um novo cliente no sistema com confirmacao de salvamento.
     * @param scanner Scanner para entrada de dados do usuario.
     */
    private void cadastrarCliente(Scanner scanner) {
        // Gera o ID unico para o cliente
        int novoIdCliente = Clientes.gerarProximoIdCliente(listaClientes); // Usa a lista interna
        // Chama o metodo cadastrarCliente da classe Clientes, passando o ID gerado
        Clientes novoCliente = Clientes.cadastrarCliente(novoIdCliente); 
        if (novoCliente != null) {
            listaClientes.add(novoCliente); // Adiciona a lista interna
            System.out.println("Cliente '" + novoCliente.getNome() + "' (ID: " + novoCliente.getId() + ") cadastrado em memoria.");
            
            System.out.print("Deseja salvar este cliente no arquivo? (S/N): ");
            String confirmarSalvar = scanner.nextLine().trim();
            
            if (confirmarSalvar.equalsIgnoreCase("S")) {
                salvarDadosClientes(); // Salva a lista completa
                System.out.println("Cliente salvo com sucesso no arquivo!");
            } else {
                System.out.println("Cliente nao salvo no arquivo. Ele permanecera na sessao atual e sera perdido ao sair.");
            }
        } else {
            System.out.println("Cadastro de cliente cancelado ou invalido.");
        }
    }

    /**
     * Edita um cliente existente na lista.
     * @param scanner O scanner para entrada do usuario.
     */
    private void editarCliente(Scanner scanner) {
        if (listaClientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado para editar.");
            return;
        }

        listarClientes();
        System.out.print("\nDigite o ID (inteiro) do cliente a editar: ");
        int idEditar = scanner.nextInt(); 
        scanner.nextLine();

        Clientes clienteParaEditar = buscarClientePorId(idEditar); 

        if (clienteParaEditar != null) {
            Clientes.editarCliente(clienteParaEditar); // Edita o objeto cliente
            salvarDadosClientes(); // Salva apos edicao
            System.out.println("Cliente editado com sucesso e salvo no arquivo!");
        } else {
            System.out.println("Cliente com ID '" + idEditar + "' nao encontrado!");
        }
    }

    /**
     * Exclui um cliente da lista.
     * @param scanner O scanner para entrada do usuario.
     */
    private void excluirCliente(Scanner scanner) {
        if (listaClientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado para excluir.");
            return;
        }

        listarClientes();
        System.out.print("\nDigite o ID (inteiro) do cliente que deseja excluir: ");
        int idExcluir = scanner.nextInt(); 
        scanner.nextLine();

        Clientes clienteParaExcluir = buscarClientePorId(idExcluir); 

        if (clienteParaExcluir != null) {
            System.out.print("Tem certeza que deseja excluir " + clienteParaExcluir.getNome() + "? (S/N): ");
            String confirmacao = scanner.nextLine();
            if (confirmacao.equalsIgnoreCase("S")) {
                listaClientes.remove(clienteParaExcluir); 
                salvarDadosClientes(); // Salva apos exclusao
                System.out.println("Cliente excluido com sucesso e salvo no arquivo!");
            } else {
                System.out.println("Exclusao de cliente cancelada.");
            }
        } else {
            System.out.println("Cliente com ID '" + idExcluir + "' nao encontrado!");
        }
    }

    /**
     * Exibe a lista de clientes cadastrados com seus respectivos IDs.
     */
    public void listarClientes() { // Metodo publico para ser chamado de fora
        if (listaClientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

         System.out.println("\n=== LISTA DE CLIENTES ===");
        for (Clientes c : listaClientes) {
            System.out.println("ID: " + c.getId() + " | Nome: " + c.getNome() + " | CPF: " + c.getCpfAnonimizado());
        }
    }
    
    /**
     * Busca um cliente na lista pelo seu ID.
     * @param idCliente O ID do cliente a ser buscado.
     * @return O objeto Clientes se encontrado, ou null caso contrario.
     */
    public Clientes buscarClientePorId(int idCliente) {
        for (Clientes c : listaClientes) {
            if (c.getId() == idCliente) {
                return c;
            }
        }
        return null;
    }

    /**
     * Carrega a lista de clientes a partir do arquivo JSON.
     * @return Uma lista de objetos Clientes.
     */
    private List<Clientes> carregarDadosClientes() {
        List<Clientes> clientes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CLIENTES_JSON))) {
            Gson gson = new Gson();
            clientes = gson.fromJson(reader, new TypeToken<List<Clientes>>(){}.getType());
            if (clientes == null) {
                clientes = new ArrayList<>();
            }
            System.out.println("GerenciadorClientes: Clientes carregados. Total: " + clientes.size()); 
        } catch (FileNotFoundException e) {
            System.out.println("GerenciadorClientes: Arquivo '" + ARQUIVO_CLIENTES_JSON + "' nao encontrado. Criando lista de clientes vazia.");
            clientes = new ArrayList<>();
        } catch (IOException e) {
            System.err.println("GerenciadorClientes: Erro ao carregar dados de clientes: " + e.getMessage());
        }
        return clientes;
    }

    /**
     * Salva a lista de clientes no arquivo JSON.
     */
    public void salvarDadosClientes() { // Metodo publico para ser chamado de fora
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CLIENTES_JSON))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(listaClientes, writer);
            System.out.println("GerenciadorClientes: Clientes salvos com sucesso. Total: " + listaClientes.size()); 
        } catch (IOException e) {
            System.err.println("GerenciadorClientes: Erro ao salvar dados de clientes: " + e.getMessage());
        }
    }
}
